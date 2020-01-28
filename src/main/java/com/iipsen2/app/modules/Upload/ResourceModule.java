package com.iipsen2.app.modules.Upload;

import com.iipsen2.app.interfaces.ModuleMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Upload.dao.ResourceDao;
import com.iipsen2.app.modules.Upload.resources.UploadResource;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class ResourceModule implements ModuleMethods {
    public static final ModuleType MODULE_TYPE = ModuleType.RESOURCE;
    private static ResourceDao resourceDao;

    public ResourceModule(DBI jdbi) {
        ResourceModule.resourceDao = jdbi.onDemand(ResourceDao.class);
    }

    @Override
    public void registerModuleResources(Environment environment) {
        environment.jersey().register(
            new UploadResource()
        );
    }

    public static ResourceDao getDao() {
        return resourceDao;
    }
}
