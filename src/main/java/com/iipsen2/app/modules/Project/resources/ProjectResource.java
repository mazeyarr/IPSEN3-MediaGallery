package com.iipsen2.app.modules.Project.resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.iipsen2.app.filters.bindings.AuthBinding;
import com.iipsen2.app.interfaces.enums.LikeType;
import com.iipsen2.app.modules.Project.filters.bindings.ProjectCreatorBinding;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.models.ProjectSimple;
import com.iipsen2.app.modules.Project.resources.params.ProjectCreateParams;
import com.iipsen2.app.modules.Project.resources.params.ProjectUpdateParams;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.services.TagService;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.ExceptionService;
import com.iipsen2.app.utility.LookupUtil;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/project")
@Produces({MediaType.APPLICATION_JSON})
public class ProjectResource {

    @GET
    @AuthBinding
    @Path("/all")
    public List<ProjectSimple> getSimpleProjectAll() {
        long startTime = System.currentTimeMillis();

        List<ProjectSimple> projectSimples = ProjectService.findSimpleProjectAll();

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;

        System.err.println("Execution time in milliseconds: " + timeElapsed);
        return projectSimples;
    }

    @GET
    @Path("/all/full")
    @AuthBinding
    public List<Project> getProjectAll() {
        return ProjectService.findProjectAll();
    }

    @GET
    @Path("/excellent")
    public List<ProjectSimple> getProjectExcellent() {
        return ProjectService.findSimpleProjectAll()
                .stream()
                .filter((ProjectSimple::isExcellent))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/search")
    @AuthBinding
    public List<Project> getProjectByTitle(
        @QueryParam("searchString") String searchString,
        @QueryParam("caseSensitive") boolean caseSensitive
    ) {
        System.out.println(caseSensitive);
        return ProjectService.searchProjectByTitle(searchString, caseSensitive);
    }

    @GET
    @Path("/{id}")
    @AuthBinding
    public Project getProjectById(@PathParam("id") long id) {
        return ProjectService.findProjectById(id);
    }

    @POST
    @Path("/create")
    @AuthBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Project postProjectCreate(@Valid @BeanParam ProjectCreateParams projectCreateParams) {
        Project newProject = new Project(
            projectCreateParams.getTitle(),
            projectCreateParams.getLanguage(),
            projectCreateParams.getGrade(),
            UserService.getAuthUser(),
            projectCreateParams.getEducation()
        );

        return ProjectService.createProject(newProject);
    }

    @PUT
    @Path("/update/{id}")
    @AuthBinding
    @ProjectCreatorBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Project putProjectUpdate(@Valid @BeanParam ProjectUpdateParams projectUpdateParams) {
        Project project = new Project(
            projectUpdateParams.getId(),
            projectUpdateParams.getTitle(),
            projectUpdateParams.getLanguage(),
            projectUpdateParams.getGrade(),
            UserService.getAuthUser(),
            projectUpdateParams.getEducation()
        );

        ProjectService.updateProject(project);

        return ProjectService.findProjectById(project.getId());
    }

    @DELETE
    @Path("/delete/{id}")
    @AuthBinding
    @ProjectCreatorBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteProject(
        @FormParam("id") long id
    ) {
        if (ProjectService.deleteProjectById(id)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @PUT
    @Path("/tag/{id}/{tagId}")
    @AuthBinding
    @ProjectCreatorBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Project putProjectTag(
        @PathParam("id") long projectId,
        @PathParam("tagId") long tagId
    ) {
        Project project = ProjectService.findProjectById(projectId);
        Tag tag = TagService.findTagById(tagId);

        if (tagId == tag.getId()) {
            ProjectService.addTagToProject(project, tag);
        } else {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Tagging failed: Tag was not found!",
                "Tagging failed: Tag id was null",
                Response.Status.BAD_REQUEST
            );
        }

        return project;
    }

    @PUT
    @Path("/tag/{id}")
    @AuthBinding
    @ProjectCreatorBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Project putProjectTag(
        @FormParam("id") long projectId,
        @FormParam("tag") String tag
    ) {
        Project project = ProjectService.findProjectById(projectId);
        List<Tag> tags = TagService.findTagByName(tag);

        if (tags.size() < 1) {
            Tag newTag = TagService.createTag(tag);

            ProjectService.addTagToProject(project, newTag);
        } else {
            ProjectService.addTagToProject(project, tags.get(0));
        }

        return project;
    }

    @DELETE
    @Path("/tag/{id}/{tagId}")
    @AuthBinding
    @ProjectCreatorBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Project deleteProjectTag(
        @PathParam("id") long projectId,
        @PathParam("tagId") long tagId
    ) {
        Project project = ProjectService.findProjectById(projectId);
        Tag tag = TagService.findTagById(tagId);

        if (tagId == tag.getId()) {
            ProjectService.removeTagFromProject(project, tag);
        } else {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Tagging failed: Tag was not found!",
                "Tagging failed: Tag id was null",
                Response.Status.BAD_REQUEST
            );
        }

        return project;
    }

    @PUT
    @Path("/like/{id}")
    @AuthBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Project putProjectLike(
        @FormParam("id") long projectId,
        @FormParam("likeType") String likeType
    ) {
        Project project = ProjectService.findProjectById(projectId);

        if (LookupUtil.lookup(LikeType.class, likeType)) {
            LikeType validatedLikeType = LikeType.valueOf(likeType);

            if (project.isValid()) {
                ProjectService.addLikeToProject(project, validatedLikeType);
            } else {
                ExceptionService.throwIlIllegalArgumentException(
                    this.getClass(),
                    "Liking Project failed: Project was not found!",
                    "Liking Project failed: Project id was null",
                    Response.Status.BAD_REQUEST
                );
            }
        } else {
            ExceptionService.throwIlIllegalArgumentException(
                LookupUtil.class,
                "Liking Project failed: Type of like was unknown!",
                "Liking Project failed: Could not convert likeType",
                Response.Status.BAD_REQUEST
            );
        }

        return project;
    }
}
