package com.iipsen2.app.modules.Tag.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class TagSimple {
    @JsonProperty
    private String name;

    /**
     * Null InstituteBean
     */
    public TagSimple() {
    }

    /**
     * Mapping InstituteBean
     * @param name
     */
    public TagSimple(
            String name
    ) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
