package com.grmek.rso.imagemanaging;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.kumuluz.ee.logs.cdi.Log;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users/{userId}/albums/{albumId}/images")
@Log
public class ImageResource {

    @Inject
    private ConfigurationProperties cfg;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addNewImage(@PathParam("userId") int userId,
                                @PathParam("albumId") int albumId,
                                @Context HttpServletRequest request) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM albums WHERE id = "
                                             + albumId + " AND user_id = " + userId);
        ) {
            if (rs.next()) {
                /* Upload the image to the GCP Storage and get the url. */
                String url = gcpUpload(request.getPart("image-file"));

                /* Add image entry to the DB. */
                String imageName = new Scanner(request.getPart("image-name").getInputStream())
                        .useDelimiter("\\A")
                        .next();

                stmt.executeUpdate("INSERT INTO images (name, url, album_id) VALUES ('"
                                   + imageName + "', '" + url + "', '"+ albumId + "')");

                /* TODO: Use the image processing MS. */
            }
            else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        catch (Exception e) {
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
                        String url = rs2.getString(3);

                        /* Remove entry from the DB. */
                        stmt.executeUpdate("DELETE FROM images WHERE id = "
                                           + imageId + " AND album_id = " + albumId);

                        /* Delete the image from the GCP Storage by the url. */
                        gcpDelete(url);

                        /* TODO: Delete all comments of the image. */

                        /* TODO: Delete all image processing data of the image. */
                    }
                }
                catch (Exception e) {
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

    private String gcpUpload(Part filePart) throws Exception {
        /* Check file by the name. */
        String fileName = filePart.getSubmittedFileName();

        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            throw new Exception("Invalid file name.");
        }

        String fileNameExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!(fileNameExtension.equals("jpg") || fileNameExtension.equals("png"))) {
            throw new Exception("File should be either jpg or png.");
        }

        /* Get the storage object. */
        Storage storage = StorageOptions
                .newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(cfg.getGcpKey().getBytes())))
                .setProjectId(cfg.getGcpProject())
                .build()
                .getService();

        /* Generate new file name for storage and prepare the file for uploading. */
        String storageFileName = UUID.randomUUID().toString() + "." + fileNameExtension;

        InputStream fileStream = filePart.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readBuf = new byte[4096];
        while (fileStream.available() > 0) {
            int bytesRead = fileStream.read(readBuf);
            outputStream.write(readBuf, 0, bytesRead);
        }
        byte[] storageFileData = outputStream.toByteArray();

        /* Upload and return the url. */
        storage.create(BlobInfo.newBuilder(cfg.getGcpStorageBucket(), storageFileName).build(), storageFileData);

        return "https://storage.cloud.google.com/" + cfg.getGcpStorageBucket() + "/" + storageFileName;
    }

    private void gcpDelete(String url) throws IOException {
        /* Check the url. */
        if (url == null || url.isEmpty() || !url.contains("/")) {
            return;
        }

        /* Get the storage object. */
        Storage storage = StorageOptions
                .newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(cfg.getGcpKey().getBytes())))
                .setProjectId(cfg.getGcpProject())
                .build()
                .getService();

        /* Get the blob id and delete the blob. */
        String blobName = url.substring(url.lastIndexOf('/') + 1);
        BlobId blobId = BlobId.of(cfg.getGcpStorageBucket(), blobName);
        storage.delete(blobId);
    }
}
