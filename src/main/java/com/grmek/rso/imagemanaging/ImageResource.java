package com.grmek.rso.imagemanaging;

import com.kumuluz.ee.logs.cdi.Log;
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
@Path("users/{userId}/albums/{albumId}/images")
@Log
public class ImageResource {

    @Inject
    private ConfigurationProperties cfg;

    @POST
    public Response addNewImage(@PathParam("userId") int userId,
                                @PathParam("albumId") int albumId,
                                Image image) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                             + albumId + " AND user_id = " + userId);
        ) {
            if (rs.next()) {
                /* TODO: Upload the image and get the url. */

                String url = "http://myurl.com/img.png";

                stmt.executeUpdate("INSERT INTO images (name, url, album_id) VALUES ('"
                                   + image.getName() + "', '" + url + "', '"+ albumId + "')");

                /* TODO: Use the image processing MS. */
            }
            else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
    
    @GET
    public Response getAllImages(@PathParam("userId") int userId,
                                 @PathParam("albumId") int albumId) {
        List<Image> images = null;

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                              + albumId + " AND user_id = " + userId);
        ) {
            if (rs1.next())
            {
                images = new LinkedList<Image>();

                try (
                    ResultSet rs2 = stmt.executeQuery("SELECT * FROM images WHERE album_id = " + albumId);
                ) {
                    while (rs2.next()) {
                        Image image = new Image();
                        image.setId(rs2.getString(1));
                        image.setName(rs2.getString(2));
                        image.setUrl(rs2.getString(3));
                        images.add(image);
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

        if (images != null) {
            return Response.ok(images).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{imageId}")
    public Response getImage(@PathParam("userId") int userId,
                             @PathParam("albumId") int albumId,
                             @PathParam("imageId") int imageId) {
        Image image = null;

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                              + albumId + " AND user_id = " + userId);
        ) {
            if (rs1.next()) {
                try (
                    ResultSet rs2 = stmt.executeQuery("SELECT * FROM images WHERE id = "
                                                      + imageId + " AND album_id = " + albumId);
                ) {
                    if (rs2.next()) {
                        image = new Image();
                        image.setId(rs2.getString(1));
                        image.setName(rs2.getString(2));
                        image.setUrl(rs2.getString(3));
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

        if (image != null) {
            return Response.ok(image).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{imageId}")
    public Response deleteImage(@PathParam("userId") int userId,
                                @PathParam("albumId") int albumId,
                                @PathParam("imageId") int imageId) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                              + albumId + " AND user_id = " + userId);
        ) {
            if (rs1.next()) {
                try (
                    ResultSet rs2 = stmt.executeQuery("SELECT * FROM images WHERE id = "
                                                      + imageId + " AND album_id = " + albumId);
                ) {
                    if (rs2.next()) {
                        stmt.executeUpdate("DELETE FROM images WHERE id = "
                                           + imageId + " AND album_id = " + albumId);

                        /* TODO: Delete the image at the url. */

                        /* TODO: Delete all comments of the image. */

                        /* TODO: Delete all image processing data of the image. */
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

        return Response.noContent().build();
    }
}
