package com.iipsen2.app.filters;

import com.iipsen2.app.MainService;
import com.iipsen2.app.filters.bindings.AdminBinding;
import com.iipsen2.app.filters.bindings.AuthBinding;
import com.iipsen2.app.interfaces.enums.UserRoleType;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.ExceptionService;
import com.iipsen2.app.utility.LookupUtil;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Authentication Filter that prevents users to use resources that they are not permitted for
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
@Provider
@AdminBinding
@Priority(Priorities.AUTHENTICATION + 1)
public class AdminFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        boolean isAdmin = UserService.getAuthUser().getRoles().stream().anyMatch(
            (userRole -> UserRoleType.valueOf(userRole.getRole()) == UserRoleType.ADMIN)
        );

        if (!isAdmin) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Authorization Failed: You are not an ADMINISTRATOR!",
                "Authorization Failed: User is not of ADMIN type...",
                Response.Status.FORBIDDEN
            );
        }
    }
}
