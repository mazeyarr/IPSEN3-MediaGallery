package com.iipsen2.app.modules.Tag.dao.seeders;

import com.iipsen2.app.interfaces.abstracts.SeederMethods;
import com.iipsen2.app.modules.Tag.TagModule;
import com.iipsen2.app.modules.Tag.dao.TagDao;
import com.iipsen2.app.modules.Tag.services.TagService;
import com.iipsen2.app.services.CoreService;

public class TagTableSeeder extends CoreService implements SeederMethods {

    public TagTableSeeder(boolean seed) {
        if (seed) {
            run();
        } else {
            reset();
        }
    }

    @Override
    public boolean isAlreadySeeded() {
        try {
            return TagService.findTagById(1)
                    .getName().equals("Scriptie");
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void run() {
        if (!isAlreadySeeded()) {
            TagService.createTag("Scriptie");
            TagService.createTag("Rekenen");
            TagService.createTag("Technisch");
            TagService.createTag("Scheikunde");
            TagService.createTag("Formules");
            TagService.createTag("Algebra");
            TagService.createTag("Wiskunde A");
            TagService.createTag("Wiskunde B");
            TagService.createTag("HBO");
            TagService.createTag("ICT");
            TagService.createTag("Samenvatting");
            TagService.createTag("Verslag");
        }
    }

    @Override
    public void reset() {

    }
}
