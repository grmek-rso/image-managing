package com.grmek.rso.imagemanaging;

import com.kumuluz.ee.logs.cdi.Log;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users/{userId}/albums")
@Log
public class AlbumResource {

    @POST
    public Response addNewAlbum(@PathParam("userId") String userId,
                                Album album) {
        /* TODO: ... */
        return Response.noContent().build();
    }
    
    @GET
    public Response getAllAlbums(@PathParam("userId") String userId) {
        /* TODO: ... */
        List<Album> albums = new LinkedList<Album>();
        return Response.ok(albums).build();
    }

    @GET
    @Path("{albumId}")
    public Response getAlbum(@PathParam("userId") String userId,
                             @PathParam("albumId") String albumId) {
        /* TODO: ... */
        Album album = new Album();

        if (album != null)
        {
            return Response.ok(album).build();
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{albumId}")
    public Response deleteAlbum(@PathParam("userId") String userId,
                                @PathParam("albumId") String albumId) {
        /* TODO: ... */
        return Response.noContent().build();
    }
}
