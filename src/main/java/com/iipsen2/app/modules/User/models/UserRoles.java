package com.iipsen2.app.modules.User.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserRoles Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class UserRoles {
    @JsonProperty
    private long id;

    @JsonProperty
    private long userId;

    @JsonProperty
    private String role;

    public UserRoles(long id, long userId, String role) {
        this.id = id;
        this.userId = userId;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
