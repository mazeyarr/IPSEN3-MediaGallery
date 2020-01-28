package com.iipsen2.app.modules.Project.dao.mappers;

import com.iipsen2.app.modules.User.UserModule;
import com.iipsen2.app.modules.User.models.User;
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
public class ProjectLikeMapper implements ResultSetMapper<User> {
    @Override
    public User map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return UserModule.getDao().findUserById(r.getLong("user_id"));
    }
}