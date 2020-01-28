package com.iipsen2.app.modules.Project.dao.seeders;

import com.iipsen2.app.interfaces.abstracts.SeederMethods;
import com.iipsen2.app.interfaces.enums.LikeType;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Education.services.EducationService;
import com.iipsen2.app.modules.Project.dao.ProjectDao;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.services.ProjectService;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.services.TagService;
import com.iipsen2.app.modules.User.dao.seeders.UserTableSeeder;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.CoreService;
import com.iipsen2.app.utility.NumberUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProjectTableSeeder extends CoreService implements SeederMethods {
    private static final int TOTAL_PROJECTS_TO_ADD = 400;

    private List<String> languages = new ArrayList<>();;
    private List<String> titles = new ArrayList<>();;

    public ProjectTableSeeder(boolean seed) {
        //remove else block replace with function call
        if (seed) {
            run();
        } else {
            reset();
        }
    }

    private void initLanguages() {
       // loop over array insteade of calling them indevidually or make a variabke for each language
        languages.add("Dutch");
        languages.add("English");
        languages.add("French");
        languages.add("German");
        languages.add("Spanish");
        languages.add("Chinese");
    }
//add throws declaration and handle error in a different methode
    private void initTestData() {
        final int MOVIE_TITLE_COLUMN = 11;
        int totalLeftRecordsToAdd = TOTAL_PROJECTS_TO_ADD;

        String csvFile = "src/main/resources/test_data_movies.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null && totalLeftRecordsToAdd != 0) {
                // use comma as separator
                String[] movie = line.split(cvsSplitBy);

                titles.add(movie[MOVIE_TITLE_COLUMN]);

                totalLeftRecordsToAdd--;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isAlreadySeeded() {
        try {
            //maggic number why one
            //try to avoid calling a methode on the same line as you call another methode
            return ProjectService.findProjectById(1).getId() == 1;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void likeAllProjectsRandomly() {
        final int startingAtId = UserTableSeeder.DEFAULT_USERS + 1;
        ProjectService.findProjectAll().forEach(project -> {
            final   int likesToGive = NumberUtil.getRandom(startingAtId, UserTableSeeder.TOTAL_RANDOM_USERS);

            for (int givenLikes = startingAtId; givenLikes < likesToGive; givenLikes++) {
                //split below in variables
                getDao(ModuleType.PROJECT, ProjectDao.class).addLikeToProjectByIds(
                        givenLikes,
                        LikeType.LIKE,
                        project.getId()
                );
            }
        });
    }

    public void createProjects() {
        Random r = new Random();
        List<Education> educations = EducationService.findEducationAll();
        List<Tag> tags = TagService.findTagAll();

        Project newProjectDefault = new Project(
                "Excellent Project!",
                "Dutch",
                8f,
                UserService.getUserById(1),
                educations.get(r.nextInt(educations.size()))
        );
        ProjectService.createProject(newProjectDefault);
//magic number
        for (int testProjectNumber = 1; testProjectNumber < TOTAL_PROJECTS_TO_ADD; testProjectNumber++) {
            Project newProject = new Project(
                    titles.get(r.nextInt(titles.size())),
                    languages.get(r.nextInt(languages.size())),
                    (r.nextFloat() * (10 - 1)) + 1,
                    UserService.getUserById(1),
                    educations.get(r.nextInt(educations.size()))
            );

            Project project = ProjectService.createProject(newProject);

            ProjectService.addTagToProject(project, tags.get(r.nextInt(tags.size())));
            ProjectService.addTagToProject(project, tags.get(r.nextInt(tags.size())));
        }
    }

    @Override
    public void run() {
        if (!isAlreadySeeded()) {
            initLanguages();
            initTestData();
            createProjects();
            likeAllProjectsRandomly();
        }
    }

    @Override
    public void reset() {

    }
}
