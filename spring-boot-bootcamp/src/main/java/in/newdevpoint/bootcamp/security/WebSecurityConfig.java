package in.newdevpoint.bootcamp.security;

import in.newdevpoint.bootcamp.security.jwt.AuthEntryPointJwt;
import in.newdevpoint.bootcamp.security.jwt.AuthTokenFilter;
import in.newdevpoint.bootcamp.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// @EnableWebSecurity
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  private static final String[] SWAGGER_WHITELIST = {
    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
  }; // Endpoints for Swagger documentation that are publicly accessible

  private static final String[] OTHER_WHITELIST = {
    "/api/auth/**", "/system/**"
  }; // Other endpoints that are publicly accessible, such as authentication and test endpoints

  private static final String[] ADMIN_RESTRICT = {"/admin/**"};

  // Injecting custom implementation of UserDetailsService for loading user-specific data
  @Autowired UserDetailsServiceImpl userDetailsService;

  // Injecting the authentication entry point to handle unauthorized access attempts
  @Autowired private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  // @Override
  // public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws
  // Exception {
  //
  // authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  // }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  // @Bean
  // @Override
  // public AuthenticationManager authenticationManagerBean() throws Exception {
  //  return super.authenticationManagerBean();
  // }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // @Override
  // protected void configure(HttpSecurity http) throws Exception {
  //  http.cors().and().csrf().disable()
  //    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
  //    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
  //    .authorizeRequests().antMatchers("/api/auth/**").permitAll()
  //    .antMatchers("/api/test/**").permitAll()
  //    .anyRequest().authenticated();
  //
  //  http.addFilterBefore(authenticationJwtTokenFilter(),
  // UsernamePasswordAuthenticationFilter.class);
  // }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // Disable CSRF protection as it's not needed for stateless APIs
    http.csrf(csrf -> csrf.disable());

    // Handle unauthorized access exceptions
    http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));

    // Configure session management to be stateless (no session creation)
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // Configure authorization rules
    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(
                    OTHER_WHITELIST) // Allow access to endpoints in the OTHER_WHITELIST
                .permitAll()
                .requestMatchers(SWAGGER_WHITELIST) // Allow access to Swagger-related endpoints
                .permitAll()
                .requestMatchers(ADMIN_RESTRICT)
                .hasAnyAuthority("ROLE_ADMIN")
                .anyRequest() // All other requests must be authenticated
                .authenticated());

    // Use the custom authentication provider for authentication
    http.authenticationProvider(authenticationProvider());

    // Add JWT token filter before the username/password authentication filter
    http.addFilterBefore(
        authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
