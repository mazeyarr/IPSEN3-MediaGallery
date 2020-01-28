package com.iipsen2.app.modules.Education.services;

import com.iipsen2.app.modules.Education.EducationModule;
import com.iipsen2.app.modules.Education.dao.EducationDao;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Institute.InstituteModule;
import com.iipsen2.app.modules.Institute.dao.InstituteDao;
import com.iipsen2.app.modules.Institute.models.Institute;
import com.iipsen2.app.services.CoreService;

import java.util.List;

public class EducationService extends CoreService {
    public static Education createEducation(String title, Institute institute) {
        long educationId = getDao().createEducation(
                new Education(title, institute),
                institute
        );

        return getDao().findEducationById(educationId);
    }

    public static List<Education> findEducationAll() {
        return getDao().findEducationAll();
    }

    public static Education findEducationById(long id) {
        return getDao().findEducationById(id);
    }

    public static void updateEducation(Education education) {
        getDao().updateEducation(
                education,
                education.getInstitute()
        );
    }

    public static void deleteEducationById(long id) {
        getDao().deleteEducationById(id);
    }

    private static EducationDao getDao() {
        return getDao(EducationModule.MODULE_TYPE, EducationDao.class);
    }
}
