package com.eurodyn.qlack.util.filter;


import static java.util.Collections.emptyList;

import com.eurodyn.qlack.util.JWTUtil;
import com.eurodyn.qlack.util.dto.JWTClaimsRequestDTO;
import com.eurodyn.qlack.util.dto.JWTClaimsResponseDTO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

  private String secret;

  public JWTAuthenticationFilter(String secret) {
    this.secret = secret;
  }

  private Authentication getAuthentication(HttpServletRequest request) {
    final JWTClaimsResponseDTO jwtClaimsResponseDTO = JWTUtil
        .getClaims(new JWTClaimsRequestDTO(JWTUtil.getRawToken(request), secret));
    if (jwtClaimsResponseDTO != null && StringUtils.isNotBlank(jwtClaimsResponseDTO.getSubject())) {
      return new UsernamePasswordAuthenticationToken(jwtClaimsResponseDTO.getSubject(), null,
          emptyList());
    } else {
      return null;
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    Authentication authentication = getAuthentication((HttpServletRequest) request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }
}
