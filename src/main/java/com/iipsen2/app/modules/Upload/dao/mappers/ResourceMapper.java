package com.iipsen2.app.modules.Upload.dao.mappers;

import com.iipsen2.app.modules.Project.ProjectModule;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Upload.models.Resource;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps the attributes from the Institute table in the database to Java Objects
 * Joins likes to this object
 *
 * @author Mazeyar Reazaei
 * @since 11-12-2019
 */
public class ResourceMapper implements ResultSetMapper<Resource> {
    @Override
    //betete naamgevking hier
    public Resource map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return new Resource(
                r.getLong("upload_id"),
                r.getString("filename"),
                r.getString("path"),
                r.getString("mime"),
                r.getString("extension"),
                ProjectModule.getDao().findProjectById(r.getLong("project_id"))
        );
    }
}
