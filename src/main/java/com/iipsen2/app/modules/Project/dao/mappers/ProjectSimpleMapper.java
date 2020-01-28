package com.iipsen2.app.modules.Project.dao.mappers;

import com.iipsen2.app.modules.Education.EducationModule;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.models.ProjectSimple;
import com.iipsen2.app.modules.User.UserModule;
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
public class ProjectSimpleMapper implements ResultSetMapper<ProjectSimple> {
    @Override
    public ProjectSimple map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return new ProjectSimple(
                r.getLong("project_id"),
                r.getString("title"),
                r.getString("language"),
                r.getFloat("grade"),
                UserModule.getDao().findUserById(r.getLong("created_user_id")),
                EducationModule.getDao().findEducationById(r.getLong("education_id"))
        );
    }
}