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
@Path("users")
@Log
public class UserResource {

    @Inject
    private ConfigurationProperties cfg;

    @Inject
    @RestClient
    private CommentingRestService commentingService;

    @Inject
    private SharingGrpcService sharingService;

    @POST
    public Response addNewUser(User user) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
        ) {
            stmt.executeUpdate("INSERT INTO users (name) VALUES ('" + user.getName() + "')");
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
    
    @GET
    public Response getAllUsers() {
        List<User> users = new LinkedList<User>();

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        ) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString(1));
                user.setName(rs.getString(2));
                users.add(user);
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(users).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@PathParam("userId") int userId) {
        User user = null;

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = " + userId);
        ) {
            if (rs.next()) {
                user = new User();
                user.setId(rs.getString(1));
                user.setName(rs.getString(2));
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (user != null) {
            return Response.ok(user).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") int userId) {
        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = " + userId);
        ) {
            if (rs.next()) {
                /* Remove entry from the DB. */
                stmt.executeUpdate("DELETE FROM users WHERE id = " + userId);

                /* Delete all comments for the user. */
                commentingService.userCleanUp(userId);

                /* Delete all album sharing data for the user. */
                sharingService.userCleanUp(userId);
            }
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
}
