package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.service.VerificationService;
import io.swagger.annotations.Api;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author European Dynamics
 */
@Path("/verification")
@Api(value = "Verification API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class VerificationWS {

    @Context
    private HttpHeaders httpHeaders;

    private final VerificationService verificationService;

    @Autowired
    public VerificationWS(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @POST
    @Path("/token/create")
    public String createVerificationToken(@QueryParam("userId") String userId, @QueryParam("expiresOn") long expiresOn,
        @QueryParam("data") String data) throws ServiceException {
        return verificationService.createVerificationToken(userId, expiresOn, data);
    }

}

