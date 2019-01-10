package com.eurodyn.qlack.fuse.demo.war.ws;

import com.eurodyn.qlack.common.annotation.ResourceAccess;
import com.eurodyn.qlack.common.annotation.ResourceOperation;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import io.swagger.annotations.Api;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author European Dynamics
 */
@Path("/demo")
@Api(value = "Demo User API")
@Consumes("application/json")
@Produces("application/json")
@Component
public class DemoWS {
    @Context
    private HttpHeaders httpHeaders;

    private final UserService userService;

    @Autowired
    public DemoWS(UserService userServiceService) {
        this.userService = userServiceService;
    }

    @POST
    @Path("/create")
    @ResourceAccess(
        operations = {
            @ResourceOperation(operation = "CREATE")
        })
    public String create(UserDTO user) throws ServiceException {
        return userService.createUser(user);
    }

    @DELETE
    @Path("/delete/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @ResourceAccess(operations = { @ResourceOperation(operation = "DELETE", resourceIdParameter = "id") })
    public void delete(@PathParam("id") String id) throws ServiceException {
        userService.deleteUser(id);
    }

}
