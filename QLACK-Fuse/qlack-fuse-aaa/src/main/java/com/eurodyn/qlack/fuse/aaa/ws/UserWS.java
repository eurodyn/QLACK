package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author European Dynamics
 */

@Path("/user")
@Api(value = "QLACK User API")
@Component
public class UserWS {

    @Context
    private HttpHeaders httpHeaders;

    private final UserService userService;

    @Autowired
    public UserWS(UserService userServiceService) {
        this.userService = userServiceService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public String create(UserDTO user) throws ServiceException {
        return userService.createUser(user);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public void update(UserDTO user, boolean updatePassword) throws ServiceException {
        userService.updateUser(user, updatePassword);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public UserDTO read(@PathParam("id") String id) throws ServiceException {
        return userService.getUserById(id);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id}")
    public void delete(@PathParam("id") String id) throws ServiceException {
        userService.deleteUser(id);
    }

}
