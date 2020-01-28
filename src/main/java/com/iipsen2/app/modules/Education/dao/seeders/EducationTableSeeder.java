package com.iipsen2.app.modules.Education.dao.seeders;

import com.iipsen2.app.interfaces.abstracts.SeederMethods;
import com.iipsen2.app.modules.Education.EducationModule;
import com.iipsen2.app.modules.Education.dao.EducationDao;
import com.iipsen2.app.modules.Education.models.Education;
import com.iipsen2.app.modules.Education.services.EducationService;
import com.iipsen2.app.modules.Institute.InstituteModule;
import com.iipsen2.app.modules.Institute.dao.InstituteDao;
import com.iipsen2.app.modules.Institute.models.Institute;
import com.iipsen2.app.modules.Institute.services.InstituteService;
import com.iipsen2.app.services.CoreService;

import java.util.List;
import java.util.Random;

public class EducationTableSeeder extends CoreService implements SeederMethods {

    public EducationTableSeeder(boolean seed) {
        if (seed) {
            run();
        } else {
            reset();
        }
    }

    @Override
    public boolean isAlreadySeeded() {
        try {
            return EducationService.findEducationById(1)
                    .getTitle().equals("Economie");
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void run() {
        if (!isAlreadySeeded()) {
            final int TOTAL_INSTITUTES = 4;
            Random r = new Random();
            List<Institute> institutes = InstituteService.findInstituteAll();
            EducationService.createEducation(
                    "Economie",
                    institutes.get(r.nextInt(TOTAL_INSTITUTES))
            );
            ;
            EducationService.createEducation(
                    "Informatica",
                    institutes.get(r.nextInt(TOTAL_INSTITUTES))
            );
            ;
            EducationService.createEducation(
                    "Zorg en Welzijn",
                    institutes.get(r.nextInt(TOTAL_INSTITUTES))
            );
            ;
            EducationService.createEducation(
                    "Bio-Informatica",
                    institutes.get(r.nextInt(TOTAL_INSTITUTES))
            );
        }
    }

    @Override
    public void reset() {

    }
}
