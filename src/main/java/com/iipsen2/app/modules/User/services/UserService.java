package com.iipsen2.app.modules.User.services;

import com.iipsen2.app.MainService;
import com.iipsen2.app.interfaces.enums.UserRoleType;
import com.iipsen2.app.modules.User.UserModule;
import com.iipsen2.app.modules.User.dao.UserDao;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.models.UserRoles;
import com.iipsen2.app.services.CoreService;
import com.iipsen2.app.services.ExceptionService;

import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class UserService extends CoreService {
    private static User AuthUser;

    public static User getAuthenticatedUserBy(String username, String password) {
        try {
            User authUser = getDao().findUserByUsername(username);

            proceedIfUserNotNull(authUser);
            proceedIfPasswordIsValid(password, authUser.getPassword());

            List<UserRoles> authUserRoles = getDao().findUserRolesByUserId(authUser.getId());

            authUser.setRoles(authUserRoles);
            authUser.setJwt(
                    MainService.tokenProvider
                            .generateToken(authUser.getId())
            );

            return authUser;

        } catch (Exception e) {
            ExceptionService.throwIlIllegalArgumentException(
                    UserService.class,
                    "Failed to get authenticated user",
                    e.getCause().getMessage(),
                    Response.Status.CONFLICT
            );
        }

        return new User();
    }

    public static User getUserById(long id) {
        User user = getDao().findUserById(id);
        if (user.isValidUser()) {
            List<UserRoles> authUserRoles = getDao().findUserRolesByUserId(
                user.getId()
            );

            user.setRoles(authUserRoles);
        } else {
            user = new User();
        }

        return user;
    }

    public static User createUser(User user, UserRoleType role) {
        long userId  = getDao().createUser(user);

        getDao().addRoleToUser(userId, role);

        return getUserById(userId);
    }

    public static User addRoleToUser(long userId, UserRoleType userRoleType) {
        User user = getUserById(userId);

        if (user.isValidUser()) {
            boolean isSameRole = user.getRoles().stream().anyMatch(
                (userRole -> UserRoleType.valueOf(userRole.getRole()) == userRoleType)
            );

            if (!isSameRole) {
                getDao().addRoleToUser(userId, userRoleType);
            }
        }

        return user;
    }

    public static User deleteRoleFromUser(long userId, UserRoleType userRoleType) {
        User user = getUserById(userId);

        if (user.isValidUser()) {
            boolean isSameRole = user.getRoles().stream().anyMatch(
                (userRole -> UserRoleType.valueOf(userRole.getRole()) == userRoleType)
            );

            if (isSameRole) {
                getDao().deleteRoleFromUser(userId, userRoleType);
            }
        }

        return user;
    }

    public static User getAuthenticatedUserBy() {
        return AuthUser;
    }

    public static void setAuthUser(User authUser) {
        AuthUser = authUser;
    }

    private static UserDao getDao() {
        return getDao(UserModule.MODULE_TYPE, UserDao.class);
    }

    private static void proceedIfUserNotNull(User user) throws Exception {
        if (user == null) throw new Exception("User does not exist");
    }

    private static void proceedIfPasswordIsValid(String givenPassword, String password) throws Exception {
        if (!PasswordDecryptService.validatePassword(givenPassword, password)) {
            throw new Exception("Given password is invalid!");
        }
    }
}
