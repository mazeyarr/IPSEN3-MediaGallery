package com.iipsen2.app.modules.Upload.dao;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Upload.dao.mappers.ResourceMapper;
import com.iipsen2.app.modules.Upload.models.Resource;
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
public interface ResourceDao extends MainDao {
    @SqlQuery("select * from uploads")
    @Mapper(ResourceMapper.class)
    List<Resource> findResourceAll();

    @SqlQuery("select * from uploads where upload_id = :id")
    @Mapper(ResourceMapper.class)
    Resource findResourceById(@Bind("id") long resourceId);

    @SqlQuery("select * from uploads where project_id = :id")
    @Mapper(ResourceMapper.class)
    Resource findResourceByProjectId(@Bind("id") long projectId);

    @SqlUpdate("insert into uploads (filename, path, mime, extension, project_id) values (:upload.filename, :upload.path, :upload.mime, :upload.extension, :project.id)")
    @GetGeneratedKeys
    long createProjectResource(
            @BindBean("upload") Resource resource,
            @BindBean("project") Project project
    );

    @SqlUpdate("update uploads set filename = :upload.filename, path = :upload.path, mime = :upload.mime, extension = :upload.extension, project_id = :project.id where upload_id = :upload.id")
    void updateResource(
            @BindBean("upload") Resource resource,
            @BindBean("project") Project project
    );

    @SqlUpdate("delete from uploads where upload_id = :id")
    void deleteResourceById(@Bind("id") long id);
}
