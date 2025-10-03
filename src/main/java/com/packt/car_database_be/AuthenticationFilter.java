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

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, java.io.IOException {
        // Get JSON Web Token from Authorization header.
        String jws = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (jws != null) {
            // Verify JSON Web Token and get user.
            String user = jwtService.getAuthorisedUser(httpServletRequest);
            // Authenticate
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, java.util.Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
