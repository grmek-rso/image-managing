package com.grmek.rso.imagemanaging;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users/{userId}/albums")
@Log
public class AlbumResource {

    @Inject
    private ConfigurationProperties cfg;

    @Inject
    @RestClient
    private CommentingRestService commentingService;

    @Inject
    private SharingGrpcService sharingService;

    @POST
    public Response addNewAlbum(@PathParam("userId") int userId,
                                Album album) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
        ) {
            stmt.executeUpdate("INSERT INTO albums (name, user_id) VALUES ('"
                               + album.getName() + "', '" + userId + "')");
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
    
    @GET
    public Response getAllAlbums(@PathParam("userId") int userId) {
        List<Album> albums = null;

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM users WHERE id = " + userId);
        ) {
            if (rs1.next())
            {
                albums = new LinkedList<Album>();

                try (
                    ResultSet rs2 = stmt.executeQuery("SELECT * FROM albums WHERE user_id = " + userId);
                ) {
                    while (rs2.next()) {
                        Album album = new Album();
                        album.setId(rs2.getString(1));
                        album.setName(rs2.getString(2));
                        albums.add(album);
                    }
                }
                catch (SQLException e) {
                    System.err.println(e);
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (albums != null) {
            return Response.ok(albums).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{albumId}")
    public Response getAlbum(@PathParam("userId") int userId,
                             @PathParam("albumId") int albumId) {
        Album album = null;

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                             + albumId + " AND user_id = " + userId);
        ) {
            if (rs.next()) {
                album = new Album();
                album.setId(rs.getString(1));
                album.setName(rs.getString(2));
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (album != null) {
            return Response.ok(album).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{albumId}")
    public Response deleteAlbum(@PathParam("userId") int userId,
                                @PathParam("albumId") int albumId) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                             + albumId + " AND user_id = " + userId);
        ) {
            if (rs.next()) {
                /* Remove entry from the DB. */
                stmt.executeUpdate("DELETE FROM albums WHERE id = " + albumId + " AND user_id = " + userId);

                /* Delete all comments for the album. */
                commentingService.albumCleanUp(albumId);

                /* Delete all album sharing data for the album. */
                sharingService.albumCleanUp(albumId);
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
}
