package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributesDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.service.AccountingService;
import io.swagger.annotations.Api;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("/accounting")
@Api(value = "Accounting API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AccountingWS {

    @Context
    private HttpHeaders httpHeaders;

    private final AccountingService accountingService;

    @Autowired
    public AccountingWS(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @POST
    @Path("/session/create")
    public String createSession(SessionDTO sessionDTO) throws ServiceException {
        return accountingService.createSession(sessionDTO);
    }

    @GET
    @Path("/session/terminate")
    public void terminateSession(String sessionId) throws ServiceException {
        accountingService.terminateSession(sessionId);
    }

    @POST
    @Path("/session/attributes/update")
    public void updateAttributes(SessionAttributesDTO attribCol, @QueryParam("createIfMissing") boolean createIfMissing)
        throws ServiceException {
        accountingService.updateAttributes(attribCol.getAttributes(), createIfMissing);
    }

}
