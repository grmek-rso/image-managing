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
public interface CommentingRestService {

    @DELETE
    @Path("user-clean-up")
    Response userCleanUp(@QueryParam("user") int userId);

    @DELETE
    @Path("album-clean-up")
    Response albumCleanUp(@QueryParam("album") int albumId);

    @DELETE
    @Path("image-clean-up")
    Response imageCleanUp(@QueryParam("image") int imageId);
}
