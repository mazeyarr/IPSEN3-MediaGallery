package com.iipsen2.app.modules.Institute.dao.seeders;

import com.iipsen2.app.interfaces.abstracts.SeederMethods;
import com.iipsen2.app.modules.Institute.services.InstituteService;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.services.CoreService;

public class InstituteTableSeeder extends CoreService implements SeederMethods {

    public InstituteTableSeeder(boolean seed) {
        if (seed) {
            run();
        } else {
            reset();
        }
    }

    @Override
    public boolean isAlreadySeeded() {
        try {
            return InstituteService.findInstituteById(1)
                    .getName().equals("Hogeschool Leiden");
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void run() {
        if (!isAlreadySeeded()) {
            InstituteService.createInstitute("Hogeschool Leiden", "Leiden");

            InstituteService.createInstitute("Universiteit van Leiden", "Leiden");

            InstituteService.createInstitute("Hogeschool van Amsterdam", "Amsterdam");

            InstituteService.createInstitute("VU", "Amsterdam");

            InstituteService.createInstitute("UvA", "Amsterdam");
        }
    }

    @Override
    public void reset() {

    }
}
