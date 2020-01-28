package com.iipsen2.app.modules.Institute.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class Institute {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String location;

    /**
     * Null InstituteBean
     */
    public Institute() {
    }

    /**
     * Mapping InstituteBean
     * @param id
     * @param name
     * @param location
     */
    public Institute(
            long id,
            String name,
            String location
    ) {
        this.setId(id);
        this.setName(name);
        this.setLocation(location);
    }

    /**
     * Creating a InstituteBean
     *
     * @param name
     * @param location
     */
    public Institute(
            String name,
            String location
    ) {
        this.setName(name);
        this.setLocation(location);
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
