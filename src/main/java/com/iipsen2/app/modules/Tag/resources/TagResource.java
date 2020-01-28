package com.iipsen2.app.modules.Tag.resources;

import com.iipsen2.app.filters.bindings.AuthBinding;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.services.TagService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/tag")
@Produces({MediaType.APPLICATION_JSON})
public class TagResource {
    @GET
    @AuthBinding
    @Path("/all")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Map<String, Long> getTagAll() {
        Map<String, Long> tagStatistics = new HashMap<>();
        List<Tag> tags = TagService.findTagAll();

        tags.forEach(tag -> tagStatistics.put(
                tag.getName(),
                TagService.countTag(tag))
        );
        return tagStatistics;
    }
}
