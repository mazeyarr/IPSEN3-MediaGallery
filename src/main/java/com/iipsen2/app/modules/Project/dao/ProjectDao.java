package com.iipsen2.app.modules.Project.dao;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.interfaces.enums.LikeType;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Project.dao.mappers.*;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.models.ProjectSimple;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.models.TagSimple;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.models.UserSimple;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * Main Data Access Object interface for al the query's
 *
 * @author mazeyar
 * @since 16-11-2019
 * @version 1.0
 */
public interface ProjectDao extends MainDao {
    @SqlQuery("select * from projects")
    @Mapper(ProjectMapper.class)
    List<Project> findProjectAll();

    @SqlQuery("select * from projects")
    @Mapper(ProjectSimpleMapper.class)
    List<ProjectSimple> findSimpleProjectAll();

    @SqlQuery("select * from projects where project_id = :id")
    @Mapper(ProjectMapper.class)
    Project findProjectById(@Bind("id") long projectId);

    @SqlQuery("select * from projects where title like :title")
    @Mapper(ProjectMapper.class)
    List<Project> searchProjectByTitle(@Bind("title") String title);

    @SqlQuery("select * from projects where title like binary :title")
    @Mapper(ProjectMapper.class)
    List<Project> searchProjectByTitleCaseSensitive(@Bind("title") String title);

    @SqlQuery("select * from projects_tags where project_id = :project_id")
    @Mapper(ProjectTagMapper.class)
    List<Tag> getTagsOfProject(@Bind("project_id") long projectId);

    @SqlQuery("select * from projects_tags where project_id = :project_id")
    @Mapper(ProjectSimpleTagMapper.class)
    List<TagSimple> getSimpleTagsOfProject(@Bind("project_id") long projectId);

    @SqlQuery("select * from projects_likes where project_id = :project_id and like_type = :likeType")
    @Mapper(ProjectLikeMapper.class)
    List<User> getLikesOfProject(
        @Bind("project_id") long projectId,
        @Bind("likeType") String likeType
    );

    @SqlQuery("select * from projects_likes where project_id = :project_id and like_type = :likeType")
    @Mapper(ProjectSimpleLikeMapper.class)
    List<UserSimple> getSimpleLikesOfProject(
        @Bind("project_id") long projectId,
        @Bind("likeType") String likeType
    );

    @SqlQuery("select count(*) as like_count from projects_likes where project_id = :project_id and like_type = :likeType")
    @Mapper(ProjectMinimalLikeMapper.class)
    Long getCountOfLikesOfProject(
        @Bind("project_id") long projectId,
        @Bind("likeType") String likeType
    );

    @SqlUpdate("insert into projects (title, language, grade, created_user_id, education_id) values (:project.title, :project.language, :project.grade, :user.id, :education.id)")
    @GetGeneratedKeys
    long createProject(
            @BindBean("project") Project project,
            @BindBean("user") User createdBy,
            @BindBean("education") Education education
    );

    @SqlUpdate("update projects set title = :project.title, language = :project.language, grade = :project.grade, created_user_id = :user.id, education_id = :education.id where project_id = :project.id")
    void updateProject(
            @BindBean("project") Project project,
            @BindBean("user") User createdBy,
            @BindBean("education") Education education
    );

    @SqlUpdate("insert into projects_tags (tag_id, project_id) values (:tag.id, :project.id)")
    void addTagToProject(
            @BindBean("tag") Tag tag,
            @BindBean("project") Project project
    );

    @SqlUpdate("insert into projects_likes (user_id, like_type, project_id) values (:user.id, :likeType, :project.id)")
    void addLikeToProject(
        @BindBean("user") User user,
        @Bind("likeType") LikeType likeType,
        @BindBean("project") Project project
    );

    @SqlUpdate("insert into projects_likes (user_id, like_type, project_id) values (:userId, :likeType, :projectId)")
    void addLikeToProjectByIds(
        @Bind("userId") long userId,
        @Bind("likeType") LikeType likeType,
        @Bind("projectId") long projectId
    );

    @SqlUpdate("delete from projects_likes where user_id = :user.id and project_id = :project.id")
    void removeLikeFromProject(
        @BindBean("user") User user,
        @BindBean("project") Project project
    );

    @SqlUpdate("delete from projects_tags where tag_id = :tag.id and project_id = :project.id")
    void removeTagFromProject(
            @BindBean("tag") Tag tag,
            @BindBean("project") Project project
    );

    @SqlUpdate("delete from projects where project_id = :id")
    void deleteProjectById(@Bind("id") long id);
}
