package com.iipsen2.app.modules.User.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iipsen2.app.modules.User.services.PasswordEncryptService;
import com.iipsen2.app.services.ExceptionService;

import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class User {
    @JsonProperty
    private long id;

    @JsonProperty
    private String username;

    @JsonProperty
    private String firstname;

    @JsonProperty
    private String lastname;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jwt;

    @JsonProperty
    private List<UserRoles> roles;

    /**
     * Null UserBean
     */
    public User() {
    }

    /**
     * Mapping a UserBean
     * @param id
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     */
    public User(
            long id,
            String username,
            String password,
            String firstname,
            String lastname,
            List<UserRoles> roles
    ) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setRoles(roles);
    }

    // Creating a UserBean
    public User(
            String username,
            String password,
            String firstname,
            String lastname
    ) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstname(firstname);
        this.setLastname(lastname);
    }

    @JsonIgnore
    public boolean isValidUser() {
        return this.getId() != 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public List<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoles> roles) {
        this.roles = roles;
    }
}
