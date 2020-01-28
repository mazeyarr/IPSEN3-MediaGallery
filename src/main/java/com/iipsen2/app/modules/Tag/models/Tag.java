package com.iipsen2.app.modules.Tag.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class Tag {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    /**
     * Null InstituteBean
     */
    public Tag() {
    }

    /**
     * Mapping InstituteBean
     * @param id
     * @param name
     */
    public Tag(
            long id,
            String name
    ) {
        this.setId(id);
        this.setName(name);
    }

    /**
     * Creating a InstituteBean
     *
     * @param name
     */
    public Tag(
            String name
    ) {
        this.setName(name);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
