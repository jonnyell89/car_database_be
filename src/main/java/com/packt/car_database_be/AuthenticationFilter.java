package com.packt.car_database_be;

import com.packt.car_database_be.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

// Intercepts HTTP requests before they reach the controllers and responds to their JSON Web Tokens.
@Component // Marks the class as a Spring-managed component; can be injected using constructor injection.
public class AuthenticationFilter extends OncePerRequestFilter { // Guarantees the filter executes once per HTTP request.

    // Dependency injection. Validates and parses JSON Web Tokens from HTTP requests.
    private final JwtService jwtService;

    // When creating an AuthenticationFilter object, Spring injects an instance of the JwtService.
    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Is applied to every incoming HTTP request.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        // Get JSON Web Token from the HTTP request Authorization header.
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Extracts the JWT substring from the header string.
            String username = jwtService.getAuthorisedUser(token); // Returns the username embedded in the subject of the JWT.

            if (username != null) {
                // Creates an Authentication object.
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                // Provides Spring Security with the identity of the authenticated user.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Passes the request to the next filter in the chain, or the controller itself.
        filterChain.doFilter(request, response);
    }
}
