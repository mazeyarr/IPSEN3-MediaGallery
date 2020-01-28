package com.iipsen2.app.modules.Education.dao.mappers;

import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Institute.InstituteModule;
import com.iipsen2.app.modules.Institute.models.Institute;
import com.iipsen2.app.modules.Institute.services.InstituteService;
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
public class EducationMapper implements ResultSetMapper<Education> {
    @Override
    public Education map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return new Education(
                r.getLong("education_id"),
                r.getString("title"),
                InstituteModule.getDao().findInstituteById(r.getLong("institute_id"))
        );
    }
}