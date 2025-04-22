package in.newdevpoint.bootcamp.controller;

import in.newdevpoint.bootcamp.entity.ERole;
import in.newdevpoint.bootcamp.entity.Role;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.payload.request.LoginRequest;
import in.newdevpoint.bootcamp.payload.request.SignupRequest;
import in.newdevpoint.bootcamp.payload.response.JwtResponse;
import in.newdevpoint.bootcamp.payload.response.MessageResponse;
import in.newdevpoint.bootcamp.repository.RoleRepository;
import in.newdevpoint.bootcamp.repository.UserRepository;
import in.newdevpoint.bootcamp.security.jwt.JwtUtils;
import in.newdevpoint.bootcamp.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Authenticates a user with the provided credentials and returns a JWT token along with user details.
     *
     * @param loginRequest the login credentials containing username and password
     * @return a response entity containing the JWT token, user ID, username, email, and assigned roles
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles =
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    /**
     * Creates and persists all roles defined in the {@code ERole} enum.
     *
     * @return HTTP 201 response with a message indicating successful role creation
     */
    @PostMapping("/createRoleList")
    public ResponseEntity<?> createRoleList() {
        List<Role> roleList = Arrays.stream(ERole.values()).map(Role::new).collect(Collectors.toList());

        for (Role role : roleList) {
            roleRepository.save(role);
        }

         return ResponseEntity
          .status(HttpStatus.CREATED)
            .body(new MessageResponse("Roles created successfully"));
    }

    /**
     * Registers a new user with the provided signup details.
     *
     * Validates that the username and email are unique, encodes the password, assigns roles (defaulting to user if none specified), and saves the new user to the repository.
     * Returns an appropriate HTTP response indicating success or failure.
     *
     * @param signUpRequest the signup request containing username, email, password, and optional roles
     * @return HTTP 200 with a success message if registration is successful, or HTTP 400 with an error message if the username or email already exists
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user =
                new UserEntity(
                        signUpRequest.getUsername(),
                        signUpRequest.getEmail(),
                        encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole =
                    roleRepository
                            .findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(
                    role -> {
                        switch (role) {
                            case "admin":
                                Role adminRole =
                                        roleRepository
                                                .findByName(ERole.ROLE_ADMIN)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(adminRole);

                                break;
                            case "mod":
                                Role modRole =
                                        roleRepository
                                                .findByName(ERole.ROLE_MODERATOR)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(modRole);

                                break;
                            default:
                                Role userRole =
                                        roleRepository
                                                .findByName(ERole.ROLE_USER)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(userRole);
                        }
                    });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
