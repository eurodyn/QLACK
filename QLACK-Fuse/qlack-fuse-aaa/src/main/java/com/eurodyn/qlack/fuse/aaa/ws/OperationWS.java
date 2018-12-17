package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.service.OperationService;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("/operation")
@Component
public class OperationWS {

    @Context
    private HttpHeaders httpHeaders;

    private final OperationService operationService;

    @Autowired
    public OperationWS(OperationService operationService) {
        this.operationService = operationService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public String create(OperationDTO operation) throws ServiceException {
        return operationService.createOperation(operation);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public OperationDTO read(@PathParam("id") String id) throws ServiceException {
        return operationService.getOperationByID(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public void update(OperationDTO operation) throws ServiceException {
        operationService.updateOperation(operation);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public void delete(@PathParam("id") String id) throws ServiceException {
        operationService.deleteOperation(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/name/{operationName}")
    public OperationDTO getOperationByName(@PathParam("operationName") String operationName) throws ServiceException {
        return operationService.getOperationByName(operationName);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/permitted")
    public Boolean isPermittedForUser(@QueryParam("userId") String userId, @QueryParam("operationName") String operationName,
        @QueryParam("resourceObjectId") String resourceObjectId) throws ServiceException {
        return operationService.isPermitted(userId, operationName, resourceObjectId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/group/permitted")
    public Boolean isPermittedForGroup(@QueryParam("groupId") String groupId, @QueryParam("operationName") String operationName,
        @QueryParam("resourceObjectId") String resourceObjectId) throws ServiceException {
        return operationService.isPermittedForGroup(groupId, operationName, resourceObjectId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/name/{operationName}/allowedgroups")
    public Set<String> getAllowedGroupsForOperation(@PathParam("operationName") String operationName) throws ServiceException {
        return operationService.getAllowedGroupsForOperation(operationName, false);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/name/{operationName}/allowedgroups/ancestors")
    public Set<String> getAllowedGroupsAncestorsForOperation(@PathParam("operationName") String operationName) throws ServiceException {
        return operationService.getAllowedGroupsForOperation(operationName, true);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resources")
    public Set<ResourceDTO> getResourcesForOperation(
        @QueryParam("userId") String userId,
        @QueryParam("getAllowed") boolean getAllowed,
        @QueryParam("getAllowed") boolean checkUserGroups,
        String... operations) throws ServiceException {
            return operationService.getResourceForOperation(userId, getAllowed, checkUserGroups, operations);
    }



}
