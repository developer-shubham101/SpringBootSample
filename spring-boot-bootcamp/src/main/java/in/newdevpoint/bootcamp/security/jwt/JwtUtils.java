package in.newdevpoint.bootcamp.security.jwt;

import in.newdevpoint.bootcamp.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${auth.jwtSecret}")
  private String jwtSecret; // Secret key used for signing and verifying JWT tokens

  @Value("${auth.jwtExpirationMs}")
  private int jwtExpirationMs;

  /**
   * Generates a JWT token for the authenticated user.
   *
   * <p>The token's subject is set to the user's username, with the issued and expiration dates
   * determined by the current time and configured expiration period.
   *
   * @param authentication the authentication object containing the user's details
   * @return a signed JWT token string
   */
  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Decodes the base64-encoded JWT secret and returns an HMAC SHA-256 signing key.
   *
   * @return a cryptographic key for signing and verifying JWT tokens
   */
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  /**
   * Extracts the username from the subject claim of a JWT token.
   *
   * @param token the JWT token to parse
   * @return the username contained in the token's subject claim
   */
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /**
   * Validates a JWT token by verifying its signature and structure.
   *
   * @param authToken the JWT token to validate
   * @return true if the token is valid; false otherwise
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
