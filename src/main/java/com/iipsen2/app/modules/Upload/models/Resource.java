package com.iipsen2.app.modules.Upload.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iipsen2.app.modules.Project.models.Project;

/**
 * User Model
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
public class Resource {
    @JsonIgnore
    public static final int PUBLIC_URL_EXPIRATION_TIME_IN_MINUTES = 15;

    @JsonProperty
    private long id;

    @JsonProperty
    private String filename;

    @JsonProperty
    private String path;

    @JsonProperty
    private String mime;

    @JsonProperty
    private String extension;

    @JsonProperty
    private Project project;

    /**
     * Null InstituteBean
     */
    public Resource() {
    }

    /**
     * Mapping InstituteBean
     * @param id
     */
    public Resource(
            long id,
            String filename,
            String path,
            String mime,
            String extension,
            Project project

    ) {
        this.setId(id);
        this.setFilename(filename);
        this.setPath(path);
        this.setMime(mime);
        this.setExtension(extension);
        this.setProject(project);
    }

    /**
     * Creating a InstituteBean
     *
     */
    public Resource(
            String filename,
            String path,
            String mime,
            String extension,
            Project project
    ) {
        this.setFilename(filename);
        this.setPath(path);
        this.setMime(mime);
        this.setExtension(extension);
        this.setProject(project);
    }

    public boolean isValid() {
        return this.getId() != 0 ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
