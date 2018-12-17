package com.eurodyn.qlack.fuse.security.war.ws;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.service.AuthenticationService;
import com.eurodyn.qlack.util.jwt.JWTUtil;
import com.eurodyn.qlack.util.jwt.dto.JWTGenerateRequestDTO;
import io.swagger.annotations.Api;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/auth")
@Api("Authentication API")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class AuthWS {

    @Value("${security.jwt.secret:aqlacksecret}")
    private String jwtSecret;

    /**
     * Default expiration set at 24 hours.
     */
    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int jwtExpiration;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @Autowired
    public AuthWS(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @POST
    @Path("/login")
    public Response login(UserDetailsDTO user) throws ServiceException {
        UserDetailsDTO authenticatedUser = (UserDetailsDTO) authenticationService
            .authenticate(user.getUsername(), user.getPassword())
            .getPrincipal();

        String userId = authenticatedUser.getId();
        UserDTO loggedUser = userService.login(userId, user.getSessionId(), true);
        String jwt = JWTUtil.generateToken(new JWTGenerateRequestDTO(jwtSecret, loggedUser.getUsername(), jwtExpiration));

        return Response.ok(loggedUser)
            .header(HttpHeaders.AUTHORIZATION, jwt)
            .build();
    }

    @POST
    @Path("/logout")
    public void logout(UserDTO user) throws ServiceException {
        String userId = userService.canAuthenticate(user.getUsername(), user.getPassword());
        userService.logout(userId, user.getSessionId());
    }

    @POST
    @Path("/unauthorized")
    public String unauthorizedRequest() {
        return "OK";
    }

    @POST
    @Path("/authorized")
    public String authorizedRequest() {
        return "OK";
    }

}
