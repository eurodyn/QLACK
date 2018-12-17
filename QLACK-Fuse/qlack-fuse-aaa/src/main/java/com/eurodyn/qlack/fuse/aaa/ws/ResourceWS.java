package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.service.ResourceService;
import io.swagger.annotations.Api;
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
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author European Dynamics
 */
@Path("/resource")
@Api(value = "Resource API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ResourceWS {

        @Context
        private HttpHeaders httpHeaders;

        private final ResourceService resourceService;

        @Autowired
        public ResourceWS(ResourceService resourceService) {
            this.resourceService = resourceService;
        }

        @POST
        @Path("/create")
        public String create(ResourceDTO resource) throws ServiceException {
            return resourceService.createResource(resource);
        }

        @GET
        @Path("/read/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
        public ResourceDTO read(@PathParam("id") String id) throws ServiceException {
                return resourceService.getResourceById(id);
        }

        @PUT
        @Path("/update")
        public void update(ResourceDTO resource) throws ServiceException {
            resourceService.updateResource(resource);
        }

        @DELETE
        @Path("/delete/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
        public void delete(@PathParam("id") String id) throws ServiceException {
            resourceService.deleteResource(id);
        }

    }

