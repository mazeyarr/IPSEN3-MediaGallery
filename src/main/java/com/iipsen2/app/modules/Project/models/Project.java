package com.iipsen2.app.modules.Project.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iipsen2.app.interfaces.enums.LikeType;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Upload.models.Resource;
import com.iipsen2.app.modules.Upload.models.ResourceSimple;
import com.iipsen2.app.modules.Upload.services.ResourceService;
import com.iipsen2.app.modules.User.models.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class Project {
    @JsonIgnore
    private static final DecimalFormat GRADE_DECIMAL_FORMAL = new DecimalFormat("0.0");

    @JsonProperty
    private long id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String language;

    @JsonProperty
    private Float grade;

    @JsonProperty
    private User createdBy;

    @JsonProperty
    private Education education;

    @JsonIgnore
    private ResourceSimple resource;

    /**
     * Null InstituteBean
     */
    public Project() {
    }

    /**
     * Mapping InstituteBean
     * @param id
     */
    public Project(
            long id,
            String title,
            String language,
            Float grade,
            User createdBy,
            Education education
    ) {
        this.setId(id);
        this.setTitle(title);
        this.setLanguage(language);
        this.setGrade(grade);
        this.setCreatedBy(createdBy);
        this.setEducation(education);
    }

    /**
     * Creating a InstituteBean
     *
     */
    public Project(
        String title,
        String language,
        Float grade,
        User createdBy,
        Education education
    ) {
        this.setTitle(title);
        this.setLanguage(language);
        this.setGrade(grade);
        this.setCreatedBy(createdBy);
        this.setEducation(education);
    }

    public boolean isValid() {
        return this.getId() != 0;
    }

    public boolean isExcellent() {
        final int EXCELLENT_GRADE = 8;
        return this.getGrade() >= EXCELLENT_GRADE;
    }

    @JsonGetter
    public int hasTotalLikes() {
        AtomicInteger count = new AtomicInteger();

        ProjectService.getLikesOfProject(this).forEach((
            (likeType, users) -> count.getAndSet(count.get() + users.size())
        ));

        return count.get();
    }

    @JsonGetter
    public Map<LikeType, List<User>> hasLikes() {
        return ProjectService.getLikesOfProject(this);
    }

    @JsonGetter
    public List<Tag> hasTags() {
        List<Tag> tagsOfProject = ProjectService.getTagsOfProject(this);

        if (tagsOfProject.isEmpty()) {
            return new ArrayList<>();
        } else {
            return tagsOfProject;
        }
    }

    @JsonGetter
    public ResourceSimple hasResource() {
        Resource resource = ResourceService.findResourceByProjectId(this.getId());
        if (resource != null) {
            return ResourceService.getPublicResourceUrl(
                resource
            );
        } else {
            return new ResourceSimple();
        }
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = Float.parseFloat(
                GRADE_DECIMAL_FORMAL
                        .format(grade)
                        .replace(',', '.')
        );
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public ResourceSimple getResource() {
        return resource;
    }

    public void setResource(ResourceSimple resource) {
        this.resource = resource;
    }
}
