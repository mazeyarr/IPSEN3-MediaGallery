package com.iipsen2.app.modules.User.dao.mappers.User;

import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.models.UserSimple;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps the attributes from the Users table in the database to Java Objects
 * Joins likes to this object
 *
 * @author Mazeyar Reazaei
 * @since 17-10-2019
 */
public class UserSimpleMapper implements ResultSetMapper<UserSimple> {
    @Override
    public UserSimple map(int i, ResultSet r, StatementContext ctx) throws SQLException {
        return new UserSimple(
                r.getLong("user_id")
        );
    }
}
