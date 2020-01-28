package com.iipsen2.app.modules.Tag.dao;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.modules.Tag.dao.mappers.TagCountMapper;
import com.iipsen2.app.modules.Tag.dao.mappers.TagMapper;
import com.iipsen2.app.modules.Tag.dao.mappers.TagSimpleMapper;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.models.TagSimple;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * Main Data Access Object interface for al the query's
 *
 * @author mazeyar
 * @since 16-11-2019
 * @version 1.0
 */
public interface TagDao extends MainDao {
    @SqlQuery("select * from tags")
    @Mapper(TagMapper.class)
    List<Tag> findTagAll();

    @SqlQuery("select name from tags")
    @Mapper(TagSimpleMapper.class)
    List<TagSimple> findSimpleTagAll();

    @SqlQuery("select * from tags where tag_id = :id")
    @Mapper(TagMapper.class)
    Tag findTagById(@Bind("id") long tagId);

    @SqlQuery("select name from tags where tag_id = :id")
    @Mapper(TagSimpleMapper.class)
    TagSimple findSimpleTagById(@Bind("id") long tagId);

    @SqlQuery("select count(tag_id) as tag_count from projects_tags where tag_id = :id")
    @Mapper(TagCountMapper.class)
    Long countTag(@BindBean Tag tag);

    @SqlQuery("select * from tags where name = :name")
    @Mapper(TagMapper.class)
    List<Tag> findTagByName(@Bind("name") String name);

    @SqlUpdate("insert into tags (name) values (:name)")
    @GetGeneratedKeys
    long createTag(@BindBean Tag tag);

    @SqlUpdate("update tags set name = :name, location = :location, where tag_id = :id")
    void updateTag(@BindBean Tag tag);

    @SqlUpdate("delete from tags where tag_id = :id")
    void deleteTagById(@Bind("id") long id);
}
