package com.iipsen2.app.modules.Education;

import com.iipsen2.app.interfaces.ModuleMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Education.dao.EducationDao;
import com.iipsen2.app.modules.Institute.dao.InstituteDao;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class EducationModule implements ModuleMethods {
    public static final ModuleType MODULE_TYPE = ModuleType.EDUCATION;
    private static EducationDao educationDao;

    public EducationModule(DBI jdbi) {
        EducationModule.educationDao = jdbi.onDemand(EducationDao.class);
    }

    @Override
    public void registerModuleResources(Environment environment) {
        //
    }

    public static EducationDao getDao() {
        return educationDao;
    }
}
