package com.iipsen2.app.modules.Institute.dao;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.modules.Institute.dao.mappers.InstituteMapper;
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
public interface InstituteDao extends MainDao {
    @SqlQuery("select * from institutes")
    @Mapper(InstituteMapper.class)
    List<Institute> findInstituteAll();

    @SqlQuery("select * from institutes where institute_id = :id")
    @Mapper(InstituteMapper.class)
    Institute findInstituteById(@Bind("id") long instituteId);

    @SqlUpdate("insert into institutes (name, location) values (:name, :location)")
    @GetGeneratedKeys
    long createInstitute(@BindBean Institute institute);

    @SqlUpdate("update institutes set name = :name, location = :location, where institute_id = :id")
    void updateInstitute(@BindBean Institute institute);

    @SqlUpdate("delete from institutes where institute_id = :id")
    void deleteInstituteById(@Bind("id") long id);
}
