package com.iipsen2.app.modules.Upload.services;

import com.iipsen2.app.interfaces.abstracts.UploadPaths;
import com.iipsen2.app.interfaces.enums.UploadType;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.Upload.ResourceModule;
import com.iipsen2.app.modules.Upload.dao.ResourceDao;
import com.iipsen2.app.modules.Upload.models.Resource;
import com.iipsen2.app.modules.Upload.models.ResourceSimple;
import com.iipsen2.app.services.CoreService;
import com.iipsen2.app.services.ExceptionService;
import com.iipsen2.app.utility.TimeUtil;
import liquibase.util.file.FilenameUtils;
import org.apache.commons.io.FileUtils;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ResourceService extends CoreService {
    public static Resource createResource(
            long entityId,
            UploadType uploadType,
            String filename,
            InputStream uploadedResourceStream
    ) {
        try {
            File temporaryResource = createTemporaryResource(uploadedResourceStream, uploadType, filename);
            String extension = FilenameUtils.getExtension(filename);
            String mime = Files.probeContentType(temporaryResource.toPath());

            switch (uploadType) {
                case PROJECT:
                    Project project = ProjectService.findProjectById(entityId);

                    proceedIfIdsAreTheSame(project.getId(), entityId);

                    String s3UploadKey = UploadPaths.generateUploadKey(
                        UUID.randomUUID().toString() + "." + System.currentTimeMillis() + "." + extension,
                        UploadType.PROJECT,
                        false
                    );

                    if (isResourceUploaded(s3UploadKey, temporaryResource)) {
                        Resource resource = new Resource(
                            filename,
                            s3UploadKey,
                            mime,
                            extension,
                            project
                        );

                        long resourceId = getDao().createProjectResource(
                            resource,
                            resource.getProject()
                        );


                        return getDao().findResourceById(resourceId);
                    } else {
                        deleteProjectLinkedToResource(project.getId());
                    }

                    cleanupTemporaryResourceFiles(temporaryResource);

                case AVATAR:
                default:
                    cleanupTemporaryResourceFiles(temporaryResource);
                    throw new Exception();
            }

        } catch (Exception e) {
            ExceptionService.throwIlIllegalArgumentException(
                    ResourceService.class,
                    "Could not execute because its unknown what to do with this upload",
                    "UploadType was unknown",
                    Response.Status.CONFLICT
            );

            return new Resource();
        }
    }

    public static List<Resource> findResourceAll() {
        return getDao().findResourceAll();
    }

    public static Resource findResourceById(long id) {
        return getDao().findResourceById(id);
    }

    public static Resource findResourceByProjectId(long projectId) {
        return getDao().findResourceByProjectId(projectId);
    }

    public static Resource updateResource(
        Resource resource,
        UploadType uploadType,
        InputStream updatedResourceStream,
        String filename
    ) {
        try {
            File temporaryResource = createTemporaryResource(updatedResourceStream, uploadType, filename);

            switch (uploadType) {
                case PROJECT:
                    if (isResourceDeletedFromS3(resource.getPath())) {
                        String fileExtension = FilenameUtils.getExtension(filename);
                        String fileMimeType = Files.probeContentType(temporaryResource.toPath());

                        resource.setFilename(filename);
                        resource.setExtension(fileExtension);
                        resource.setMime(fileMimeType);

                        resource.setPath(UploadPaths.generateUploadKey(
                            UUID.randomUUID().toString() + "." + System.currentTimeMillis() + "." + fileExtension,
                            uploadType,
                            false
                        ));

                        if (isResourceUploaded(resource.getPath(), temporaryResource)) {

                            getDao().updateResource(
                                resource,
                                resource.getProject()
                            );

                            cleanupTemporaryResourceFiles(temporaryResource);

                            return ResourceService.findResourceById(resource.getId());
                        }
                    }

                    cleanupTemporaryResourceFiles(temporaryResource);

                    return new Resource();
                case AVATAR:
                default:
                    cleanupTemporaryResourceFiles(temporaryResource);
                    throw new Exception();

            }
        } catch (Exception e) {
            ExceptionService.throwIlIllegalArgumentException(
                    ResourceService.class,
                    "Could not execute because its unknown what to do with this upload",
                    "UploadType was unknown",
                    Response.Status.CONFLICT
            );

            return new Resource();
        }
    }

    public static void deleteResourceById(long id) {
        Resource resource = getDao().findResourceById(id);

        if (getS3Service().deleteS3Object(resource.getPath())) {
            getDao().deleteResourceById(id);
        }
    }

    public static void deleteResourceByProjectId(long projectId) {
        Resource resource = getDao().findResourceByProjectId(projectId);

        if (resource != null && resource.isValid()) {
            if (getS3Service().deleteS3Object(resource.getPath())) {
                getDao().deleteResourceById(resource.getId());
            }
        }
    }

    public static ResourceSimple getPublicResourceUrl(Resource resource) {
        return new ResourceSimple(
            getS3Service().generatePreSignedS3ObjectUrl(
                resource.getPath(),
                TimeUtil.convertMinutesToMillis(Resource.PUBLIC_URL_EXPIRATION_TIME_IN_MINUTES)
            ),
            Calendar.getInstance().getTime(),
            new Date(
                Calendar.getInstance().getTimeInMillis()
                    +
                TimeUtil.convertMinutesToMillis(Resource.PUBLIC_URL_EXPIRATION_TIME_IN_MINUTES)
            )
        );
    }

    private static ResourceDao getDao() {
        return getDao(ResourceModule.MODULE_TYPE, ResourceDao.class);
    }

    private static File createTemporaryResource(
        InputStream inputStream,
        UploadType uploadType,
        String filename
    ) throws Exception {
        File temporaryFile = new File(UploadPaths.generateTemporaryFileSavePath(filename, uploadType));

        try {
            FileUtils.copyInputStreamToFile(inputStream, temporaryFile);

            return temporaryFile;
        } catch (IOException e) {
            // TODO: Logger
            throw new Exception("File was incorrect or corrupted...");
        }
    }

    private static void cleanupTemporaryResourceFiles(File file) {
        if(file.delete()) {
            System.out.println("-- TEMP FOLDER: Temp file deleted! --");
        } else {
            System.out.println("-- TEMP FOLDER: Failed to delete temp file! --");
        }
    }

    private static void proceedIfIdsAreTheSame(long idA, long idB) {
        if (idA != idB) {
            ExceptionService.throwIlIllegalArgumentException(
                    ResourceService.class,
                    "Error occurd when comparing ids",
                    "Creating resource, but ids didnt match",
                    Response.Status.CONFLICT
            );
        }
    }

    private static boolean isResourceUploaded(String UploadKey, File resourceFileToUpload) {
        return getS3Service().putS3Object(UploadKey, resourceFileToUpload);
    }

    private static boolean isResourceDeletedFromS3(String s3UploadKey) {
        return getS3Service().deleteS3Object(s3UploadKey);
    }

    private static void deleteProjectLinkedToResource(long projectId) {
        ProjectService.deleteProjectById(projectId);
    }
}
