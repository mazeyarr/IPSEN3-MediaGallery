package com.iipsen2.app.modules.Upload.resources;

import com.iipsen2.app.filters.bindings.AuthBinding;
import com.iipsen2.app.interfaces.abstracts.UploadPaths;
import com.iipsen2.app.interfaces.enums.UploadType;
import com.iipsen2.app.modules.Upload.models.Resource;
import com.iipsen2.app.modules.Upload.models.ResourceSimple;
import com.iipsen2.app.modules.Upload.services.ResourceService;
import com.iipsen2.app.services.CoreResourceService;
import com.iipsen2.app.services.ExceptionService;
import com.iipsen2.app.utility.NumberUtil;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/resource")
@Produces({MediaType.APPLICATION_JSON})
public class UploadResource extends CoreResourceService {
    @POST
    @Path("/upload")
    @AuthBinding
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Resource postUploadResource(
        @FormDataParam("entityId") long entityId,
        @FormDataParam("uploadType") String uploadType,
        @FormDataParam("resource") InputStream resourceInputStream,
        @FormDataParam("resource") FormDataContentDisposition resourceMetaData
    ) {
        return ResourceService.createResource(
            entityId,
            UploadType.valueOf(uploadType),
            resourceMetaData.getFileName(),
            resourceInputStream
        );
    }

    @POST
    @Path("/upload/multiple")
    @AuthBinding
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public HashMap<String, List<Resource>> postUploadMultipleResources(
        @FormDataParam("uploadType") String uploadType,
        FormDataMultiPart resourcesUpload
    ) {
        HashMap<String, List<Resource>> resourceResult = new HashMap<>();

        List<Resource> resourcesResolved = new ArrayList<>();
        List<Resource> resourcesRejected = new ArrayList<>();
        List<FormDataBodyPart> uploadEntities = resourcesUpload.getFields(UploadPaths.RESOURCE_RELATED_ENTITY_FORM_DATA_KEY);

        uploadEntities.forEach((uploadEntity -> {
            proceedUploadIfGivenEntityIdIsLong(uploadEntity.getValue());

            long uploadEntityId = Long.parseLong(uploadEntity.getValue());

            FormDataBodyPart uploadedResource = resourcesUpload.getField(
                    UploadPaths.RESOURCE_FORM_DATA_KEY + uploadEntityId
            );

            Resource resource = ResourceService.createResource(
                    uploadEntityId,
                    UploadType.valueOf(uploadType),
                    uploadedResource.getFormDataContentDisposition().getFileName(),
                    uploadedResource.getValueAs(InputStream.class)
            );

            if (resource.isValid()) {
                resourcesResolved.add(resource);
            } else {
                resource.setFilename(uploadedResource.getFormDataContentDisposition().getFileName());
                resourcesRejected.add(resource);
            }
        }));

        resourceResult.put("resourcesResolved", resourcesResolved);
        resourceResult.put("resourcesRejected", resourcesRejected);

        return resourceResult;
    }

    @POST
    @Path("/update/upload")
    @AuthBinding
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Resource postUploadUpdateResource(
        @FormDataParam("resourceId") long resourceId,
        @FormDataParam("uploadType") String uploadType,
        @FormDataParam("resource") InputStream resourceInputStream,
        @FormDataParam("resource") FormDataContentDisposition resourceMetaData
    ) {
        return ResourceService.updateResource(
            ResourceService.findResourceById(resourceId),
            UploadType.valueOf(uploadType),
            resourceInputStream,
            resourceMetaData.getFileName()
        );
    }

    @GET
    @Path("/regenerate/{id}")
    @AuthBinding
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ResourceSimple getNewResourceUrl(@PathParam("id") long resourceId) {
        return ResourceService.getPublicResourceUrl(
            ResourceService.findResourceById(resourceId)
        );
    }

    private void proceedUploadIfGivenEntityIdIsLong(String value) {
        if (!NumberUtil.isStringLong(value)) {
            ExceptionService.throwIlIllegalArgumentException(
                    this.getClass(),
                    "Upload failed: Entity ID -> '" + value + "' <- is not a number!",
                    "Upload failed, entity param was not a number",
                    Response.Status.BAD_GATEWAY
            );
        }
    }
}
