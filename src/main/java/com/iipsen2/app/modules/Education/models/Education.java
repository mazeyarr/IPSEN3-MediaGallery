package com.iipsen2.app.modules.Education.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iipsen2.app.modules.Institute.models.Institute;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class Education {
    @JsonProperty
    private long id;

    @JsonProperty
    private String title;

    @JsonProperty
    private Institute institute;

    /**
     * Null EducationBean
     */
    public Education() {
    }

    /**
     * Mapping EducationBean
     * @param id
     */
    public Education(
            long id,
            String title,
            Institute institute
    ) {
        this.setId(id);
        this.setTitle(title);
        this.setInstitute(institute);
    }

    /**
     * Creating a EducationBean
     *
     */
    public Education(
            String title,
            Institute location
    ) {
        this.setTitle(title);
        this.setInstitute(location);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
}
