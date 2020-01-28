package com.iipsen2.app.modules.Tag.services;

import com.iipsen2.app.modules.Tag.TagModule;
import com.iipsen2.app.modules.Tag.dao.TagDao;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.services.CoreService;

import java.util.List;

public class TagService extends CoreService {
    public static Tag createTag(String name) {
        long tagId = getDao().createTag(
                new Tag(name)
        );

        return getDao().findTagById(tagId);
    }

    public static List<Tag> findTagAll() {
        return getDao().findTagAll();
    }

    public static long countTag(Tag tag) {
        return getDao().countTag(tag);
    }

    public static Tag findTagById(long id) {
        return getDao().findTagById(id);
    }

    public static List<Tag> findTagByName(String name) {
        return getDao().findTagByName(name);
    }

    public static void updateTag(Tag tag) {
        getDao().updateTag(tag);
    }

    public static void deleteTagById(long id) {
        getDao().deleteTagById(id);
    }

    private static TagDao getDao() {
        return getDao(TagModule.MODULE_TYPE, TagDao.class);
    }
}
