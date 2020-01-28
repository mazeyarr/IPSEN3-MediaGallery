package com.iipsen2.app.modules.Project;

import com.iipsen2.app.interfaces.ModuleMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Project.dao.ProjectDao;
import com.iipsen2.app.modules.Project.filters.ProjectCreatorFilter;
import com.iipsen2.app.modules.Project.resources.ProjectResource;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class ProjectModule implements ModuleMethods {
    public static final ModuleType MODULE_TYPE = ModuleType.PROJECT;
    private static ProjectDao projectDao;

    public ProjectModule(DBI jdbi) {
        ProjectModule.projectDao = jdbi.onDemand(ProjectDao.class);
    }

    @Override
    public void registerModuleResources(Environment environment) {
        environment.jersey().register(
                new ProjectResource()
        );

        environment.jersey().register(
            new ProjectCreatorFilter()
        );
    }

    public static ProjectDao getDao() {
        return projectDao;
    }
}
