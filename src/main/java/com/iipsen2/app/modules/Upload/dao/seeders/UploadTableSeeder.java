package com.iipsen2.app.modules.Upload.dao.seeders;

import com.iipsen2.app.interfaces.abstracts.SeederMethods;
import com.iipsen2.app.interfaces.abstracts.UploadPaths;
import com.iipsen2.app.interfaces.enums.UploadType;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.Tag.services.TagService;
import com.iipsen2.app.modules.Upload.models.Resource;
import com.iipsen2.app.modules.Upload.services.ResourceService;
import com.iipsen2.app.services.CoreService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UploadTableSeeder extends CoreService implements SeederMethods {

    public UploadTableSeeder(boolean seed) {
        if (seed) {
            run();
        } else {
            reset();
        }
    }

    @Override
    public boolean isAlreadySeeded() {
        try {
            return ResourceService.findResourceById(1).getId() == 1;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void deleteAllProjectResourcesInBucket() {
        List<Resource> resources = ResourceService.findResourceAll();
        resources.forEach((resource -> {
            System.out.println("Starting delete: " + resource.getId() + " key = " + resource.getPath());
            ResourceService.deleteResourceById(resource.getId());
        }));
    }

    @Override
    public void run() {
        if (!isAlreadySeeded()) {
            deleteAllProjectResourcesInBucket();

            List<Project> projects = ProjectService.findProjectAll();

            File file = new File("src/main/resources/uploads/projects/TEST_PROJECT.pdf");

//            projects.forEach((
//                (Project project) -> {
//                    System.err.println("Starting Upload: " + project.getId());
//                    String filename = file.getName();
//                    ResourceService.createResource(
//                            filename,
//                            file,
//                            "",
//                            ".pdf",
//                            ProjectService.findProjectById(project.getId())
//                    );
//                })
//            );

            final int TEST_PROJECT_ID = 1;
            System.err.println("Starting Upload for project: " + TEST_PROJECT_ID);

            String filename = file.getName();
            try {
                Resource resource = ResourceService.createResource(
                    TEST_PROJECT_ID,
                    UploadType.PROJECT,
                    filename,
                    FileUtils.openInputStream(file)
                );

                ResourceService.getPublicResourceUrl(resource);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not seed Upload Table: IO EXCEPTION!");
            }
        }
    }

    @Override
    public void reset() {
        deleteAllProjectResourcesInBucket();
    }
}
