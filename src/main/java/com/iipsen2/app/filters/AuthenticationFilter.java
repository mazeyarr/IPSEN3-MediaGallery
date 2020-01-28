package com.iipsen2.app.filters;

import com.iipsen2.app.MainService;
import com.iipsen2.app.filters.bindings.AuthBinding;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.ExceptionService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
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
@AuthBinding
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        // Check if the user gave a Authorization key in the header
        if (!context.getHeaders().containsKey("Authorization")) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Authorization Failed: Token not provided",
                "Authorization key not provided",
                Response.Status.BAD_REQUEST
            );
        }

        String token = context.getHeaders().getFirst("Authorization");

        // Check if the token is not empty
        if (token.equals("")) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Authorization Failed: Token was empty",
                "Token was empty in the Authorization header key",
                Response.Status.BAD_REQUEST
            );
        }

        System.err.println(MainService.tokenProvider.verifyToken(token));

        // Validate the token
        if (!MainService.tokenProvider.verifyToken(token)) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Invalid token!",
                "Token verification failed!",
                Response.Status.UNAUTHORIZED
            );
        }

        // Save user in current session
        long id = MainService.tokenProvider.getDecodedJWT(token).getClaim("user_id").asLong();
        UserService.setAuthUser(
                UserService.getUserById(id)
        );
    }
}
