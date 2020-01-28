package com.iipsen2.app.modules.Project.filters;

import com.iipsen2.app.modules.Project.filters.bindings.ProjectCreatorBinding;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.ExceptionService;
import org.glassfish.jersey.message.internal.MediaTypes;
import org.glassfish.jersey.server.ContainerRequest;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@ProjectCreatorBinding
@Priority(1001)
public class ProjectCreatorFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (containerRequestContext instanceof ContainerRequest)
        {
            ContainerRequest request = (ContainerRequest) containerRequestContext;

            if (
                containerRequestContext.hasEntity()
                &&
                MediaTypes.typeEqual(MediaType.APPLICATION_FORM_URLENCODED_TYPE,request.getMediaType()))
            {
                request.bufferEntity();
                Form formData = request.readEntity(Form.class);

                if (!formData.asMap().containsKey("id")) {
                    ExceptionService.throwIlIllegalArgumentException(
                        this.getClass(),
                        "Project id not provided!",
                        "projectId was not provided",
                        Response.Status.UNAUTHORIZED
                    );
                }

                long projectId = Long.parseLong(formData.asMap().get("id").get(0));

                Project project = ProjectService.findProjectById(projectId);

                if (project == null) {
                    ExceptionService.throwIlIllegalArgumentException(
                        this.getClass(),
                        "Project does not exist",
                        "project == null",
                        Response.Status.BAD_REQUEST
                    );
                }

                if (project.getCreatedBy().getId() != UserService.getAuthUser().getId()) {
                    ExceptionService.throwIlIllegalArgumentException(
                        this.getClass(),
                        "You are not the creator of this project!",
                        "CreatorBy does not equal AuthUser",
                        Response.Status.FORBIDDEN
                    );
                }
            }
        }
    }
}
