package com.iipsen2.app.interfaces;

import com.iipsen2.app.modules.User.dao.UserDao;
import io.dropwizard.setup.Environment;

public interface ModuleMethods {
    void registerModuleResources(Environment environment);
}
