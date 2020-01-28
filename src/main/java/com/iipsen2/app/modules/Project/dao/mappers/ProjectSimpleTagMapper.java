package com.iipsen2.app.modules.Project.dao.mappers;

import com.iipsen2.app.modules.Tag.TagModule;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.models.TagSimple;
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
public class ProjectSimpleTagMapper implements ResultSetMapper<TagSimple> {
    @Override
    public TagSimple map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return TagModule.getDao().findSimpleTagById(r.getLong("tag_id"));
    }
}