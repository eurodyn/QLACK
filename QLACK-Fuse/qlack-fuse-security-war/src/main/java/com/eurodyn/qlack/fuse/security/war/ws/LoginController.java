package com.eurodyn.qlack.fuse.security.war.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class LoginController {

    @POST
    @Path("/unauthorized")
    public String unauthorizedRequest() {
        return "OK";
    }

    @POST
    @Path("/authorized")
    public String authorizedRequest() {
        return "OK";
    }

}
