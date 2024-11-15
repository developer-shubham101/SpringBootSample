package com.example.sbuser.security.jwt;

import com.example.sbuser.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired private JwtUtils jwtUtils;

  @Autowired private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // Extract JWT token from the Authorization header
      String jwt = parseJwt(request);

      // If JWT token exists and is valid, authenticate the user
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        // Extract username from the JWT token
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        // Load user details based on the extracted username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Create an authentication token using the user details and set it in the security context
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set the authentication in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      // Log any exceptions that occur while setting user authentication
      logger.error("Cannot set user authentication: {}", e);
    }

    // Continue the filter chain after processing the request
    filterChain.doFilter(request, response);
  }

  // Helper method to extract JWT token from the request's Authorization header
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    // Check if the Authorization header has text and starts with 'Bearer '
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7); // Return the JWT token after 'Bearer '
    }

    // Return null if no valid JWT token is found
    return null;
  }
}
