package com.eurodyn.qlack.fuse.security.providers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.util.Md5PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class AAAProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.badCredentials",
                "Bad credentials"));
        }

        String presentedPassword;

        if (getPasswordEncoder() instanceof Md5PasswordEncoder) {
            UserDetailsDTO user = (UserDetailsDTO) userDetails;
            presentedPassword = user.getSalt() + authentication.getCredentials().toString();
        } else {
            presentedPassword = authentication.getCredentials().toString();
        }

        if (!getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.badCredentials",
                "Bad credentials"));
        }
    }

}
