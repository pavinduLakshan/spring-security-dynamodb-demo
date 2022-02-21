package com.pavindulakshan.ssdynamodb.controller;

import com.pavindulakshan.ssdynamodb.model.User;
import com.pavindulakshan.ssdynamodb.payload.request.AddRoleRequest;
import com.pavindulakshan.ssdynamodb.exception.AppException;
import com.pavindulakshan.ssdynamodb.model.Role;
import com.pavindulakshan.ssdynamodb.payload.request.LoginRequest;
import com.pavindulakshan.ssdynamodb.payload.request.SignUpRequest;
import com.pavindulakshan.ssdynamodb.payload.response.ApiResponse;
import com.pavindulakshan.ssdynamodb.payload.response.JwtAuthenticationResponse;
import com.pavindulakshan.ssdynamodb.payload.UserSummary;
import com.pavindulakshan.ssdynamodb.repository.RoleRepository;
import com.pavindulakshan.ssdynamodb.repository.UserRepository;
import com.pavindulakshan.ssdynamodb.security.CurrentUser;
import com.pavindulakshan.ssdynamodb.security.JwtTokenProvider;
import com.pavindulakshan.ssdynamodb.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

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
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/user/role")
    public void addRole(@RequestBody AddRoleRequest addRoleReq){
        Role role = new Role(addRoleReq.getRoleName());
        roleRepository.save(role);
    }
}
