package com.grmek.rso.imagemanaging;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users/{userId}/albums/{albumId}/images")
public class ImageResource {

    @POST
    public Response addNewImage(@PathParam("userId") String userId,
                                @PathParam("albumId") String albumId,
                                Image image) {
        /* TODO: ... */
        return Response.noContent().build();
    }
    
    @GET
    public Response getAllImages(@PathParam("userId") String userId,
                                 @PathParam("albumId") String albumId) {
        /* TODO: ... */
        List<Image> images = new LinkedList<Image>();
        return Response.ok(images).build();
    }

    @GET
    @Path("{imageId}")
    public Response getImage(@PathParam("userId") String userId,
                             @PathParam("albumId") String albumId,
                             @PathParam("imageId") String imageId) {
        /* TODO: ... */
        Image image = new Image();

        if (image != null)
        {
            return Response.ok(image).build();
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{imageId}")
    public Response deleteImage(@PathParam("userId") String userId,
                                @PathParam("albumId") String albumId,
                                @PathParam("imageId") String imageId) {
        /* TODO: ... */
        return Response.noContent().build();
    }
}
