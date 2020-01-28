package com.iipsen2.app.modules.Education.dao;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.modules.Education.dao.mappers.EducationMapper;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Institute.models.Institute;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * Main Data Access Object interface for al the query's
 *
 * @author mazeyar
 * @since 16-11-2019
 * @version 1.0
 */
public interface EducationDao extends MainDao {
    @SqlQuery("select * from educations")
    @Mapper(EducationMapper.class)
    List<Education> findEducationAll();

    @SqlQuery("select * from educations where education_id = :id")
    @Mapper(EducationMapper.class)
    Education findEducationById(@Bind("id") long educationId);

    @SqlUpdate("insert into educations (title, institute_id) values (:education.title, :institute.id)")
    @GetGeneratedKeys
    long createEducation(
            @BindBean("education") Education education,
            @BindBean("institute") Institute institute
    );

    @SqlUpdate("update educations set title = :education.title, institute_id = :institute.id, where education_id = :education.id")
    void updateEducation(
            @BindBean("education") Education education,
            @BindBean("institute") Institute institute
    );

    @SqlUpdate("delete from educations where education_id = :id")
    void deleteEducationById(@Bind("id") long id);
}
