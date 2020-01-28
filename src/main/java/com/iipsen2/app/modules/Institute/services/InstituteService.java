package com.iipsen2.app.modules.Institute.services;

import com.iipsen2.app.modules.Institute.InstituteModule;
import com.iipsen2.app.modules.Institute.dao.InstituteDao;
import com.iipsen2.app.modules.Institute.models.Institute;
import com.iipsen2.app.services.CoreService;

import java.util.List;

public class InstituteService extends CoreService {
    public static Institute createInstitute(String name, String location) {
        long instituteId = getDao().createInstitute(
                new Institute(name, location)
        );

        return getDao().findInstituteById(instituteId);
    }

    public static List<Institute> findInstituteAll() {
        return getDao().findInstituteAll();
    }

    public static Institute findInstituteById(long id) {
        return getDao().findInstituteById(id);
    }

    public static void updateInstitute(Institute institute) {
        getDao().updateInstitute(institute);
    }

    public static void deleteInstituteById(long id) {
        getDao().deleteInstituteById(id);
    }

    private static InstituteDao getDao() {
        return getDao(InstituteModule.MODULE_TYPE, InstituteDao.class);
    }
}
