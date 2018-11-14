package com.eurodyn.qlack.fuse.security.manager;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log
@Component
public class AAAProvider implements AuthenticationProvider, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private boolean forcePrincipalAsString = false;

    private final UserService userService;

    // Set default values here.
    private List<Integer> validUserStatus = Arrays.asList(1);

    @Autowired
    public AAAProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Determine username & password
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        log.log(Level.FINE, "Requesting authentication for {0}.", new Object[]{username});

        preAuthenticationChecks((UsernamePasswordAuthenticationToken) authentication);

        // TODO maybe add user caching
        // Check if the user can be authenticated.
        String userID = userService.canAuthenticate(username, password);
        UserDTO userDTO;

        if (userID == null) {
            log.log(Level.WARNING, "User {0} could not be authenticated via username and password [credentials did not match].",
                username);

            throw new BadCredentialsException(messages.getMessage(
                "AaaProvider.badCredentials",
                "Bad credentials"));
        } else {
            userDTO = userService.getUserById(userID);
        }

        AAAUser user = dtoToUser(userDTO);

        // Check if the status of the user implies an active user.
        int userStatus = userDTO.getStatus();

        if (!validUserStatus.contains(userStatus)) {
            user.setActive(false);
            user.setStatus(userStatus);
        }

        Object principalToReturn = forcePrincipalAsString ? user.getUsername() : user;

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details.
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
            principal,
            authentication.getCredentials(),
            authoritiesMapper.mapAuthorities(user.getAuthorities()));

        result.setDetails(authentication.getDetails());

        return result;
    }

    private void preAuthenticationChecks(UsernamePasswordAuthenticationToken token) {
        // Do not try to authenticate users with empty credentials.
        if (StringUtils.isEmpty(token.getPrincipal()) || StringUtils.isEmpty(token.getCredentials())) {
            log.log(Level.WARNING, "User could not be authenticated via username and password [username or password was empty].",
                token.getPrincipal());

            throw new BadCredentialsException(messages.getMessage("AaaProvider.badCredentials", "Bad credentials"));
        }
    }

    public boolean isForcePrincipalAsString() {
        return forcePrincipalAsString;
    }

    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
    }

    /**
     * Sets the list of valid user statuses from string.
     * If the string is null or empty, the default value remains.
     *
     * @param s List of valid user statuses as concatenated string delimited with comma characters
     */
    public void setValidUserStatus(String s) {
        if (StringUtils.isEmpty(s)) {
            return;
        }

        validUserStatus = new ArrayList<>();

        for (String status : s.split(",")) {
            validUserStatus.add(Integer.parseInt(status));
        }
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    private AAAUser dtoToUser(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        AAAUser user = new AAAUser(dto.getUsername(), dto.getPassword(), new ArrayList<>());

        user.setSuperadmin(dto.isSuperadmin());
        user.setExternal(dto.isExternal());
        user.setSessionId(dto.getSessionId());
        user.setStatus((int) dto.getStatus());

        return user;
    }

}
