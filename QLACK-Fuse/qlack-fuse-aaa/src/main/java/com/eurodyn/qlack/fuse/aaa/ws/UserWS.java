package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import io.swagger.annotations.Api;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author European Dynamics
 */

@Path("/user")
@Api(value = "User API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UserWS {

    private final UserService userService;

    @Autowired
    public UserWS(UserService userServiceService) {
        this.userService = userServiceService;
    }

    @POST
    @Path("/create")
    public String create(UserDTO user) throws ServiceException {
        return userService.createUser(user);
    }

    @GET
    @Path("/read/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public UserDTO read(@PathParam("id") String id) throws ServiceException {
        return userService.getUserById(id);
    }

    @PUT
    @Path("/update")
    public void update(@QueryParam("updatePassword") boolean updatePassword, UserDTO user) throws ServiceException {
        userService.updateUser(user, updatePassword);
    }

    @DELETE
    @Path("/delete/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public void delete(@PathParam("id") String id) throws ServiceException {
        userService.deleteUser(id);
    }

}
