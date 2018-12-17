package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributesDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.service.AccountingService;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/create")
    public String createSession(SessionDTO sessionDTO) throws ServiceException {
        return accountingService.createSession(sessionDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/terminate")
    public void terminateSession(String sessionId) throws ServiceException {
        accountingService.terminateSession(sessionId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/attributes/update")
    public void updateAttributes(SessionAttributesDTO attribCol, @QueryParam("createIfMissing") boolean createIfMissing)
        throws ServiceException {
        accountingService.updateAttributes(attribCol.getAttributes(), createIfMissing);
    }

}
