package com.eurodyn.qlack.fuse.security.filters;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.util.jwt.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * An implementation of a filter that runs at least once in every request.
 * It checks if there is a header that contains a JWT. If exists
 * @author EUROPEAN DYNAMICS SA
 */
@Log
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Value("${security.jwt.secret:aqlacksecret}")
    private String jwtSecret;

    /**
     * Default expiration set at 24 hours.
     */
    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int jwtExpiration;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationProvider, "An AuthenticationProvider is required");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // Get the token without the prefix.
        String token = JWTUtil.getRawToken(request);

        // If the token doesn't exist continue with the next filter.
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Validate the token.
            Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .setAllowedClockSkewSeconds(jwtExpiration)
                .parseClaimsJws(token)
                .getBody();

            String username = claims.getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetailsDTO userDetails = (UserDetailsDTO) userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities());
                authentication.setDetails(authenticationDetailsSource.buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "JWT token verification failed for address " + request.getRemoteAddr() +
                " to [" + request.getPathInfo() + "]", e);

            // In case of failure make sure to clear the context to guarantee the user won't be authenticated.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

}
