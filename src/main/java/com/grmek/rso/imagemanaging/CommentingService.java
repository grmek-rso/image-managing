package com.grmek.rso.imagemanaging;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.enterprise.context.Dependent;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("")
@RegisterRestClient(configKey="commenting")
@Dependent
public interface CommentingService {

    @DELETE
    @Path("/users/{userId}")
    Response deleteCommentsForUser(@PathParam("userId") int userId);

    @DELETE
    @Path("/albums/{albumId}")
    Response deleteCommentsForAlbum(@PathParam("albumId") int albumId);

    @DELETE
    @Path("/images/{imageId}")
    Response deleteCommentsForImage(@PathParam("imageId") int imageId);
}
