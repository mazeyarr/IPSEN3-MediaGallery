package com.iipsen2.app.modules.User;

import com.iipsen2.app.interfaces.ModuleMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.User.dao.UserDao;
import com.iipsen2.app.modules.User.resources.UserResource;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class UserModule implements  ModuleMethods{
    public static final ModuleType MODULE_TYPE = ModuleType.USER;
    private static UserDao userDao;

    public UserModule(DBI jdbi) {
        UserModule.userDao = jdbi.onDemand(UserDao.class);
    }

    @Override
    public void registerModuleResources(Environment environment) {
        environment.jersey().register(
                new UserResource()
        );
    }

    public static UserDao getDao() {
        return userDao;
    }
}
