package com.packt.car_database_be.web;

import com.packt.car_database_be.domain.AccountCredentials;
import com.packt.car_database_be.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks the class as a REST controller; handles HTTP requests and returns HTTP responses.
public class LoginController {

    // Dependency injection. Validates and parses JSON Web Tokens from HTTP requests.
    private final JwtService jwtService;

    // Dependency injection. Spring Security interface that coordinates authentication logic, such as usernames and passwords.
    private final AuthenticationManager authenticationManager;

    // When creating a LoginController object, Spring injects instances of JwtService and AuthenticationManager.
    public LoginController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // The RequestBody annotation deserializes the incoming JSON payload into an AccountCredentials object.
    @PostMapping("/login") // Exposes a POST endpoint at '/login'.
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials accountCredentials) {

        // Creates a Spring Security authentication token containing the username and password.
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(accountCredentials.username(), accountCredentials.password());

        // Creates an Authentication object representing the user.
        Authentication authentication = authenticationManager.authenticate(credentials);

        // Generates a JSON Web Token.
        String jwt = jwtService.getToken(authentication.getName());

        // Builds an HTTP response with the generated JSON Web Token.
        return ResponseEntity.ok()
                // Attaches the JWT as an Authorization header.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                // Makes the Authorization header visible to the frontend JavaScript.
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }
}
