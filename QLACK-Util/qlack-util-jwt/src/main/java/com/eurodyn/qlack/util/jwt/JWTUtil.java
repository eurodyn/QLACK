package com.eurodyn.qlack.util.jwt;

import com.eurodyn.qlack.util.jwt.dto.JWTClaimsRequestDTO;
import com.eurodyn.qlack.util.jwt.dto.JWTClaimsResponseDTO;
import com.eurodyn.qlack.util.jwt.dto.JWTGenerateRequestDTO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.util.Date;

/**
 * A utility class to generate and valiate JSON Web Tokens.
 */
public class JWTUtil {
  private static final String HEADER_STRING = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer";

  private JWTUtil() {
  }

  /**
   * Creates a new JWS based on the parameters specified.
   *
   * @param request The parameters to be used to create the JWT.
   */
  public static final String generateToken(final JWTGenerateRequestDTO request) {
    // The JWT signature algorithm to be used to sign the token.
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // Set current time.
    final long nowMillis = System.currentTimeMillis();
    final Date now = new Date(nowMillis);

    // We will sign our JWT with our ApiKey secret
    final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(
        Base64.encodeBase64String(request.getSecret().getBytes()));
    final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    // Set the JWT claims.
    final JwtBuilder builder = Jwts.builder()
        .setId(request.getId())
        .setIssuedAt(now)
        .setSubject(request.getSubject())
        .setIssuer(request.getIssuer())
        .signWith(signatureAlgorithm, signingKey);

    // If it has been specified, add the expiration for the token.
    if (request.getTtl() >= 0) {
      final long expMillis = nowMillis + request.getTtl();
      final Date exp = new Date(expMillis);
      builder.setExpiration(exp);
    }

    // Add additional claims if any.
    if (request.getClaims() != null) {
      builder.addClaims(request.getClaims());
    }

    // Builds the JWT and serializes it to a compact, URL-safe string.
    return builder.compact();
  }

  /**
   * Returns the claims found in a JWT while it is also verifying the token.
   *
   * @param request The JWT to be verified together with the secret used to sign it.
   */
  public static JWTClaimsResponseDTO getClaims(JWTClaimsRequestDTO request) {
    JWTClaimsResponseDTO response = new JWTClaimsResponseDTO();

    try {
      response.setClaims(
          Jwts.parser().setSigningKey(
              Base64.encodeBase64String(request.getSecret().getBytes()))
              .setAllowedClockSkewSeconds(request.getAllowedTimeSkew())
              .parseClaimsJws(request.getJwt()).getBody());
      response.setValid(true);
    } catch (Exception e) {
      response.setValid(false);
      response.setErrorMessage(e.getMessage());
    }

    return response;
  }

  /**
   * Returns the value of a specific claim in JWT while also verifying the JWT.
   *
   * @param jwtClaimsRequest The JWT to be verified together with the secret used to sign it.
   * @param claim The name of the claim to return.
   * @return The calue of the requested claim.
   */
  public static Object getClaimValue(JWTClaimsRequestDTO jwtClaimsRequest, String claim) {
    final JWTClaimsResponseDTO claims = getClaims(jwtClaimsRequest);
    if (claims != null && claims.getClaims() != null && claims.getClaims().containsKey(claim)) {
      return claims.getClaims().get(claim);
    } else {
      return null;
    }
  }

  /**
   * Parses a JWT token (compact or normal) and returns its String representation while it is also
   * validating the token.
   *
   * @param jwt The JWT to decode.
   * @param secret The secret used to sign the JWT.
   * @return Returns the String representation of the JWT.
   */
  public static String tokenToString(String jwt, String secret) {
    return Jwts.parser().setSigningKey(Base64.encodeBase64String(secret.getBytes())).parse(jwt)
        .toString();
  }

  public static String getRawToken(HttpServletRequest request) {
    final String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      return token.replace(TOKEN_PREFIX, "");
    } else {
      return null;
    }
  }
}
