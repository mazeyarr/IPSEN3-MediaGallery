package com.iipsen2.app.modules.Institute.dao.mappers;

import com.iipsen2.app.modules.Institute.models.Institute;
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
public class InstituteMapper implements ResultSetMapper<Institute> {
    @Override
    public Institute map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return new Institute(
                r.getLong("institute_id"),
                r.getString("name"),
                r.getString("location")
        );
    }
}