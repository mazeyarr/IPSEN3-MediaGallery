package com.iipsen2.app.modules.Project.resources.params;

import com.google.common.base.Strings;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Education.services.EducationService;
import com.iipsen2.app.modules.User.models.User;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.FormParam;

public class ProjectCreateParams {
    @NotEmpty
    private String title;

    @NotEmpty
    private String language;

    @NotEmpty
    private String grade;

    @NotEmpty
    private String educationId;

    public String getTitle() {
        return title;
    }

    @FormParam("title")
    public void setTitle(String title) {
        this.title = Strings.nullToEmpty(title).trim();
    }

    public String getLanguage() {
        return language;
    }

    @FormParam("language")
    public void setLanguage(String language) {
        this.language = Strings.nullToEmpty(language).trim();
    }

    public float getGrade() {
        return Float.parseFloat(grade.replace(',', '.'));
    }

    @FormParam("grade")
    public void setGrade(String grade) {
        this.grade = Strings.nullToEmpty(grade);
    }

    public Education getEducation() {
        return EducationService.findEducationById(
                Long.parseLong(educationId)
        );
    }

    @FormParam("educationId")
    public void setEducationId(String educationId) {
        this.educationId = Strings.nullToEmpty(educationId);
    }
}
