package com.iipsen2.app.modules.Tag;

import com.iipsen2.app.interfaces.ModuleMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Project.resources.ProjectResource;
import com.iipsen2.app.modules.Tag.dao.TagDao;
import com.iipsen2.app.modules.Tag.resources.TagResource;
import com.iipsen2.app.modules.Tag.services.TagService;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class TagModule implements ModuleMethods {
    public static final ModuleType MODULE_TYPE = ModuleType.TAG;
    private static TagDao tagDao;

    public TagModule(DBI jdbi) {
        TagModule.tagDao = jdbi.onDemand(TagDao.class);
    }

    @Override
    public void registerModuleResources(Environment environment) {
        environment.jersey().register(
                new TagResource()
        );
    }

    public static TagDao getDao() {
        return tagDao;
    }
}
