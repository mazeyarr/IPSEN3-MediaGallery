package com.iipsen2.app.modules.User.resources.params;

import com.google.common.base.Strings;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Education.services.EducationService;
import com.iipsen2.app.modules.User.services.PasswordEncryptService;
import com.iipsen2.app.services.ExceptionService;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserCreateParams {
    @Email
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    public String getUsername() {
        return username;
    }

    @FormParam("username")
    public void setUsername(String username) {
        this.username = Strings.nullToEmpty(username).trim();
    }

    public String getPassword() {
        return password;
    }

    @FormParam("password")
    public void setPassword(String password) {
        try {
            this.password = PasswordEncryptService.generateStrongPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "User Create Params: Password could not be encrypted!",
                "User Create Params: Password encryption algorithm not found!",
                Response.Status.BAD_REQUEST
            );
        } catch (InvalidKeySpecException e) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "User Create Params: Password invalid key...",
                "User Create Params: Password invalid key...",
                Response.Status.BAD_REQUEST
            );
        }
    }

    public String getFirstname() {
        return firstname;
    }

    @FormParam("firstname")
    public void setFirstname(String firstname) {
        this.firstname = Strings.nullToEmpty(firstname).trim();
    }

    public String getLastname() {
        return lastname;
    }

    @FormParam("lastname")
    public void setLastname(String lastname) {
        this.lastname = Strings.nullToEmpty(lastname).trim();
    }
}
