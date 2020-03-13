package com.example.authservice.controllers;


import com.example.authservice.models.ERole;
import com.example.authservice.models.Role;
import com.example.authservice.models.User;
import com.example.authservice.payload.request.LoginRequest;
import com.example.authservice.payload.request.SignupRequest;
import com.example.authservice.payload.response.ActivationResponse;
import com.example.authservice.payload.response.JwtResponse;
import com.example.authservice.payload.response.MessageResponse;
import com.example.authservice.repos.RoleRepository;
import com.example.authservice.repos.UserRepository;
import com.example.authservice.security.jwt.JwtUtils;
import com.example.authservice.security.services.MailSender;
import com.example.authservice.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isPresent()){
            User userFromDb = optionalUser.get();

            if (userFromDb.getActivationCode() != null){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Account is not activated!"));
            }
        }else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username or Password Incorrect!"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),

                roles));

    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        System.out.println(signUpRequest);

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null){
            addRole(roles, ERole.ROLE_STUDENT);
        }else {
            strRoles.forEach(role -> {
                if ("teacher".equals(role)) {
                    addRole(roles, ERole.ROLE_TEACHER);
                }else if ("student".equals(role)){
                    addRole(roles, ERole.ROLE_STUDENT);
                }
            });
        }


        user.setRoles(roles);
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        String message = String.format(
            "Hello, %s! \n" +
                    "Welcome to TutorFinder. Please, visit this link to activate your account: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );


        mailSender.send(user.getEmail(), "Activation Code", message);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    private void addRole(Set<Role> roles, ERole role){
        Role teacherRole = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(teacherRole);
    }
    @GetMapping("/activate/{code}")
    public ActivationResponse activate(Model model, @PathVariable String code){

        System.out.println("------------------------AUTH-SERVICE ACTIVATE IS CALLED-------------------");

        User user = userRepository.findByActivationCode(code);

        ActivationResponse activationResponse = new ActivationResponse(true);

        if (user == null){
            activationResponse.setActivated(false);
            return activationResponse;
        }

        user.setActivationCode(null);
        userRepository.save(user);


        return activationResponse;
    }

}





























