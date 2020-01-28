package com.iipsen2.app.modules.Institute;

import com.iipsen2.app.interfaces.ModuleMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Institute.dao.InstituteDao;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class InstituteModule implements ModuleMethods {
    public static final ModuleType MODULE_TYPE = ModuleType.INSTITUTE;
    private static InstituteDao instituteDao;

    public InstituteModule(DBI jdbi) {
        InstituteModule.instituteDao = jdbi.onDemand(InstituteDao.class);
    }

    @Override
    public void registerModuleResources(Environment environment) {
        //
    }

    public static InstituteDao getDao() {
        return instituteDao;
    }
}
