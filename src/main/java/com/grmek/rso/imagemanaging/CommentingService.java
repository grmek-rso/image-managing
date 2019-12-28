package com.grmek.rso.imagemanaging;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.enterprise.context.Dependent;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("v1/comments")
@RegisterRestClient(configKey="commenting")
@Dependent
public interface CommentingService {

    @DELETE
    @Path("user-clean-up")
    Response deleteCommentsForUser(@QueryParam("user") int userId);

    @DELETE
    @Path("album-clean-up")
    Response deleteCommentsForAlbum(@QueryParam("album") int albumId);

    @DELETE
    @Path("image-clean-up")
    Response deleteCommentsForImage(@QueryParam("image") int imageId);
}
