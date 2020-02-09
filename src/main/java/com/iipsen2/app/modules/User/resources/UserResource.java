package com.iipsen2.app.modules.User.resources;

import com.iipsen2.app.filters.bindings.AdminBinding;
import com.iipsen2.app.filters.bindings.AuthBinding;
import com.iipsen2.app.interfaces.enums.UserRoleType;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.resources.params.UserCreateParams;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.ExceptionService;
import com.iipsen2.app.utility.LookupUtil;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {
    /**
     * Client lands on this endpoint, using the username and password combination. And sends all the data
     * to the UserService.
     *
     * @author Mazeyar Rezaei
     * @param token
     * @return User
     */
    @POST
    @AuthBinding
    @Path("/login/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User postLoginAction(
            @FormParam("token") String token
    ) {
        return UserService.getAuthenticatedUserBy();
    }

    /**
     * Client lands on this endpoint, using the username and password combination. And sends all the data
     * to the UserService.
     *
     * @author Joeri Duijkren
     * @param username
     * @param password
     * @return
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User postLoginAction(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {

        return UserService.getAuthenticatedUserBy(username, password);
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User postCreateUserAction(
        @Valid @BeanParam UserCreateParams userCreateParams,
        @FormParam("role") String roleType
    ) {
        if (LookupUtil.lookupStringIsEnumValue(UserRoleType.class, roleType)) {
            return UserService.createUser(
                new User(
                    userCreateParams.getUsername(),
                    userCreateParams.getPassword(),
                    userCreateParams.getFirstname(),
                    userCreateParams.getLastname()
                ),
                UserRoleType.valueOf(roleType)
            );
        } else {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Create User Failed: Type of role was invalid! -> " + roleType,
                "Create User Failed: Given role parameter was invalid type -> " + roleType,
                Response.Status.BAD_REQUEST
            );

            return new User();
        }
    }

    @PUT
    @Path("/add/role")
    @AuthBinding
    @AdminBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User postAddUserRoleAction(
            @FormParam("id") long id,
            @FormParam("role") String role
    ) {
        if (LookupUtil.lookupStringIsEnumValue(UserRoleType.class, role)) {
            return UserService.addRoleToUser(id, UserRoleType.valueOf(role));
        } else {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Add Role Failed: Type of role was invalid!",
                "Add Role Failed: Given role parameter was invalid type",
                Response.Status.BAD_REQUEST
            );

            return new User();
        }
    }

    @DELETE
    @Path("/remove/role")
    @AuthBinding
    @AdminBinding
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User postRemoveUserRoleAction(
            @FormParam("id") long id,
            @FormParam("role") String role
    ) {
        if (LookupUtil.lookupStringIsEnumValue(UserRoleType.class, role)) {
            return UserService.deleteRoleFromUser(id, UserRoleType.valueOf(role));
        } else {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Remove Role Failed: Type of role was invalid!",
                "Remove Role Failed: Given role parameter was invalid type",
                Response.Status.BAD_REQUEST
            );

            return new User();
        }
    }
}
