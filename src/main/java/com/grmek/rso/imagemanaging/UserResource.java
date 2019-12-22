package com.grmek.rso.imagemanaging;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users")
public class UserResource {

    @POST
    public Response addNewUser(User user) {
        /* TODO: ... */
        return Response.noContent().build();
    }
    
    @GET
    public Response getAllUsers() {
        /* TODO: ... */
        List<User> users = new LinkedList<User>();

        User user;

        user = new User();
        user.setId("1");
        user.setName("My First User");
        users.add(user);

        user = new User();
        user.setId("2");
        user.setName("Username");
        users.add(user);

        user = new User();
        user.setId("3");
        user.setName("Test User");
        users.add(user);

        return Response.ok(users).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@PathParam("userId") String userId) {
        /* TODO: ... */
        User user = new User();

        if (user != null)
        {
            return Response.ok(user).build();
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        /* TODO: ... */
        return Response.noContent().build();
    }
}
