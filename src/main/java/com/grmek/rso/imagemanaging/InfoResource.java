package com.grmek.rso.imagemanaging;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("info")
public class InfoResource {

    @GET
    public Response getInfo() {
        return Response.ok(new Info()).build();
    }
}
