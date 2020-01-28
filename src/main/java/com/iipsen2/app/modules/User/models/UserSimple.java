package com.iipsen2.app.modules.User.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class UserSimple {
    @JsonProperty
    private long id;

    /**
     * Null UserBean
     */
    public UserSimple() {
    }

    /**
     * Mapping a UserBean
     * @param id
     */
    public UserSimple(
            long id
    ) {
        this.setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
