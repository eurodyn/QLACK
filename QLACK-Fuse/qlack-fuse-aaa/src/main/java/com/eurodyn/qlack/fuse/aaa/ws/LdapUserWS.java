package com.eurodyn.qlack.fuse.aaa.ws;

import com.eurodyn.qlack.fuse.aaa.service.LdapUserService;
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
@Path("/ldap")
@Component
public class LdapUserWS {

        @Context
        private HttpHeaders httpHeaders;

        private final LdapUserService ldapUserService;

        @Autowired
        public LdapUserWS(LdapUserService ldapUserService) {
            this.ldapUserService = ldapUserService;
        }


        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/enabled")
        public boolean isEnabled() throws ServiceException {
            return ldapUserService.isLdapEnabled();
        }

        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/authenticate")
        public String authenticate(@QueryParam("username") String username, @QueryParam("username") String password) throws ServiceException {
            return ldapUserService.canAuthenticate(username, password);
        }

    }

