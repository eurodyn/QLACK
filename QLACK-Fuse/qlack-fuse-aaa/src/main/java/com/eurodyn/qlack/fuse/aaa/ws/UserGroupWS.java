package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.GroupDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserGroupService;
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
@Path("/group")
@Component
public class UserGroupWS {

    @Context
    private HttpHeaders httpHeaders;

    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupWS(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public String create(GroupDTO group) throws ServiceException {
        return userGroupService.createGroup(group);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public GroupDTO read(@PathParam("id") String id, @QueryParam("lazyRelatives") boolean lazyRelatives) throws ServiceException {
        return userGroupService.getGroupByID(id, lazyRelatives);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public void update(GroupDTO group) throws ServiceException {
        userGroupService.updateGroup(group);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public void delete(@PathParam("id") String id) throws ServiceException {
        userGroupService.deleteGroup(id);
    }

}
