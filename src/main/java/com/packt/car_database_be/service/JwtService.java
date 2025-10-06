package com.packt.car_database_be.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// Centre of the authentication mechanism; creates, signs and verifies JSON Web Tokens.
@Component // Marks the class as a Spring-managed component; can be injected using constructor injection.
public class JwtService {

    static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds.
    // static final String PREFIX = "Bearer"; // Authorization: Bearer <JWT>

    // Only for demonstration purposes. In production, read from application.properties or environment variables.
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Creates a secret key for signing the JWT.

    // Generates a signed JSON Web Token when user login is successful.
    public String getToken(String username) {
        return Jwts.builder()
                // The subject is the main piece of identifying information stored in the JWT.
                .setSubject(username)
                // Sets the expiration data of the JWT.
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // Signs the JWT with the key.
                .signWith(key)
                // Finalises and returns the JTW as a compact string: <HEADER>.<PAYLOAD>.<SIGNATURE>
                .compact();
    }

    // Verifies and decodes the JSON Web Token.
    public String getAuthorisedUser(String token) {
        return Jwts.parserBuilder() // Creates a new JWT parser.
                .setSigningKey(key) // Provides the key to verify the signature.
                .build() // Finalises the parser configuration.
                .parseClaimsJws(token) // Parses and verifies the JWT.
                .getBody() // Retrieves the JWT payload.
                .getSubject(); // Extracts the username from the payload.
    }

//    Get JSON Web Token from request Authorization header, verify, get username.
//    public String getAuthorisedUser(HttpServletRequest httpServletRequest) {
//        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (token != null) {
//            return Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token.replace(PREFIX, ""))
//                    .getBody()
//                    .getSubject();
//        }
//        return null;
//    }
}
