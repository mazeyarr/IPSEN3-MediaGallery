package com.iipsen2.app.modules.User.dao.mappers.UserRoles;

import com.iipsen2.app.modules.User.models.UserRoles;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps the attributes from the UserRoles table in the database to Java Objects
 *
 * @author Mazeyar Reazaei
 * @since 17-10-2019
 */
public class UserRolesMapper implements ResultSetMapper<UserRoles>{
    public UserRoles map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
        return new UserRoles(
                r.getLong("user_role_id") ,
                r.getLong("user_id"),
                r.getString("role")
        );
    }
}
