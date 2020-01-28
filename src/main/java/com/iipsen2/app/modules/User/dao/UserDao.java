package com.iipsen2.app.modules.User.dao;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.interfaces.enums.UserRoleType;
import com.iipsen2.app.modules.User.dao.mappers.User.UserMapper;
import com.iipsen2.app.modules.User.dao.mappers.User.UserSimpleMapper;
import com.iipsen2.app.modules.User.dao.mappers.UserRoles.UserRolesMapper;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.models.UserRoles;
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
public interface UserDao extends MainDao {
    // User

    /**
     * Gets user by username, because username is the email it will always be unique and return one value
     * this method is usually used for validating a user
     *
     * @author Mazeyar Rezaei
     * @param username string
     * @return User object
     */
    @SqlQuery("select * from users where username = :username")
    @Mapper(UserMapper.class)
    User findUserByUsername(@Bind("username") String username);
    /**
     * Gets user by username, because username is the email it will always be unique and return one value
     * this method is usually used for validating a user
     *
     * @author Mazeyar Rezaei
     * @param username string
     * @return User object
     */
    @SqlQuery("select user_id from users where username = :username")
    @Mapper(UserSimpleMapper.class)
    UserSimple findUserSimpleByUsername(@Bind("username") String username);

    /**
     * Finds a user by the id, runs a select query with the primary id key in the where clause
     *
     * @author Mazeyar Rezaei
     * @param id long primary key
     * @return User object
     * @since 16-11-2019
     * @version 1.0
     */
    @SqlQuery("select * from users as u LEFT JOIN user_roles as ur ON ur.user_id=u.user_id where u.user_id = :id")
    @Mapper(UserMapper.class)
    User findUserById(@Bind("id") long id);
    /**
     * Finds a user by the id, runs a select query with the primary id key in the where clause
     *
     * @author Mazeyar Rezaei
     * @param id long primary key
     * @return User object
     * @since 16-11-2019
     * @version 1.0
     */
    @SqlQuery("select user_id from users as u where u.user_id = :id")
    @Mapper(UserSimpleMapper.class)
    UserSimple findUserSimpleById(@Bind("id") long id);

    /**
     * Inserts a user in the users table with all the required attributes.
     *
     * @author Jesse Minneboo
     * @param user
     * @return primary key (id) of inserted row
     */
    @SqlUpdate("insert into users (username, password, firstname, lastname) values (:username, :password, :firstname, :lastname)")
    @GetGeneratedKeys
    long createUser(@BindBean User user);

    /**
     * update a user in the users table with all the required attributes.
     *
     * @author Maarten de Koning
     * @param username string
     * @param password string
     * @param firstname string
     * @param lastname string
     * @return primary key (id) of inserted row
     */
    @SqlUpdate("update users set username = :username, password = MD5(:password), firstname = :firstname, lastname = :lastname where user_id = :id")
    @GetGeneratedKeys
    long updateUser(@BindBean User user);

    // User Roles
    /**
     * find the role of the user within the system by primary id.
     *
     * @author Mazeyar Rezaei
     * @param id long primary key
     * @return returns a list of user roles
     * @since 16-10-2019
     */
    @SqlQuery("select user_role_id, user_id, role from user_roles where user_role_id = :id")
    @Mapper(UserRolesMapper.class)
    List<UserRoles> findUserRolesById(@Bind("id") long id);

    /**
     * find the role of the user within the system by primary id of the users table user_id
     *
     * @author Mazeyar Rezaei
     * @param userId long foreign key
     * @return returns a list of user roles
     * @since 16-10-2019
     */
    @SqlQuery("select * from user_roles where user_id = :user_id")
    @Mapper(UserRolesMapper.class)
    List<UserRoles> findUserRolesByUserId(@Bind("user_id") long userId);

    /**
     * Inserts a role for the user in the user_role table.
     *
     * @author Mazeyar Rezaei
     * @param userId long foreign key
     * @param role string
     * @return primary key (id) of inserted row
     */
    @SqlUpdate("insert into user_roles (user_id, role) values (:user_id, :role)")
    @GetGeneratedKeys
    long addRoleToUser(
            @Bind("user_id") long userId,
            @Bind("role") UserRoleType role
    );
    /**
     * Inserts a role for the user in the user_role table.
     *
     * @author Mazeyar Rezaei
     * @param userId long foreign key
     * @param role string
     * @return primary key (id) of inserted row
     */
    @SqlUpdate("delete from user_roles where user_id = :user_id and role = :role")
     void deleteRoleFromUser(
            @Bind("user_id") long userId,
            @Bind("role") UserRoleType role
    );
}
