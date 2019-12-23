package com.grmek.rso.imagemanaging;

import com.kumuluz.ee.logs.cdi.Log;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("demo")
@Log
public class DemoResource {

    @Inject
    private ConfigurationProperties configurationProperties;

    @GET
    @Path("info")
    public Response getInfo() {
        return Response.ok(new Info()).build();
    }

    @GET
    @Path("etcd-parameter")
    public Response getParameter() {
        return Response.ok(configurationProperties.getDemoEtcdParameter()).build();
    }
}
