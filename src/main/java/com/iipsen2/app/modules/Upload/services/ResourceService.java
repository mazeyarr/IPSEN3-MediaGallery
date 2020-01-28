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
import com.iipsen2.app.utility.TimeUtil;
import liquibase.util.file.FilenameUtils;
import org.apache.commons.io.FileUtils;

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

                    // TODO: try / catch
                    if (project.getId() != entityId) {
                        // TODO: schedule for deletion?
                        // TODO: Throw exception!
                        return new Resource();
                    }

                    String key = UploadPaths.generateUploadKey(
                        UUID.randomUUID().toString() + "." + System.currentTimeMillis() + "." + extension,
                        UploadType.PROJECT,
                        false
                    );

                    if (getS3Service().putObject(key, temporaryResource)) {
                        Resource resource = new Resource(
                            filename,
                            key,
                            mime,
                            extension,
                            project
                        );

                        long resourceId = getDao().createProjectResource(
                            resource,
                            resource.getProject()
                        );

                        temporaryResourceCleanup(temporaryResource);

                        return getDao().findResourceById(resourceId);
                    } else {
                        temporaryResourceCleanup(temporaryResource);

                        ProjectService.deleteProjectById(project.getId());
                    }

                case AVATAR:
                    // TODO: For future scaling.
                    temporaryResourceCleanup(temporaryResource);
                default:
                    temporaryResourceCleanup(temporaryResource);

                    return new Resource();
            }

        } catch (Exception e) {
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
//varplaats try naar controller s
        try {
            File temporaryResource = createTemporaryResource(updatedResourceStream, uploadType, filename);

            switch (uploadType) {
                case PROJECT:
                    if (getS3Service().deleteObject(resource.getPath())) {
                        String extension = FilenameUtils.getExtension(filename);
                        String mime = Files.probeContentType(temporaryResource.toPath());

                        resource.setFilename(filename);
                        resource.setExtension(extension);
                        resource.setMime(mime);

                        resource.setPath(UploadPaths.generateUploadKey(
                            UUID.randomUUID().toString() + "." + System.currentTimeMillis() + "." + extension,
                            uploadType,
                            false
                        ));

                        if (getS3Service().putObject(resource.getPath(), temporaryResource)) {

                            getDao().updateResource(
                                resource,
                                resource.getProject()
                            );

                            temporaryResourceCleanup(temporaryResource);

                            return ResourceService.findResourceById(resource.getId());
                        }
                    }

                    temporaryResourceCleanup(temporaryResource);

                    return new Resource();
                case AVATAR:
                default:
                    temporaryResourceCleanup(temporaryResource);

                    return new Resource();

            }
        } catch (Exception e) {
            return new Resource();
        }
    }

    public static void deleteResourceById(long id) {
        Resource resource = getDao().findResourceById(id);

        if (getS3Service().deleteObject(resource.getPath())) {
            getDao().deleteResourceById(id);
        }
    }

    public static void deleteResourceByProjectId(long projectId) {
        Resource resource = getDao().findResourceByProjectId(projectId);

        if (resource != null && resource.isValid()) {
            if (getS3Service().deleteObject(resource.getPath())) {
                getDao().deleteResourceById(resource.getId());
            }
        }
    }

    public static ResourceSimple getPublicResourceUrl(Resource resource) {
        return new ResourceSimple(
            getS3Service().generatePreSignedObjectUrl(
                resource.getPath(),
                TimeUtil.minutesToMillis(Resource.PUBLIC_URL_EXPIRATION_TIME_IN_MINUTES)
            ),
            Calendar.getInstance().getTime(),
            new Date(
                Calendar.getInstance().getTimeInMillis()
                    +
                TimeUtil.minutesToMillis(Resource.PUBLIC_URL_EXPIRATION_TIME_IN_MINUTES)
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
        File temporaryFile = new File(UploadPaths.generateTempFilePath(filename, uploadType));
//remove try catch and throw the error instead
        try {
            FileUtils.copyInputStreamToFile(inputStream, temporaryFile);

            return temporaryFile;
        } catch (IOException e) {
            // TODO: Logger
            System.out.println("Could not creat, temporary file from uploaded resource...");

            throw new Exception("File was incorrect or corrupt...");
        }
    }

    private static void temporaryResourceCleanup(File file) {
        if(file.delete()) {
            System.out.println("-- TEMP FOLDER: Temp file deleted! --");
        } else {
            System.out.println("-- TEMP FOLDER: Failed to delete temp file! --");
        }
    }
}
