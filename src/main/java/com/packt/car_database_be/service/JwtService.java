package com.packt.car_database_be.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    // Validates a signed JSON Web Token and extracts the username from the payload.
    public String getAuthorisedUser(String token) {
        return Jwts.parserBuilder() // Creates a new JWT parser.
                .setSigningKey(key) // Provides the key to verify the signature.
                .build() // Finalises the parser configuration.
                .parseClaimsJws(token) // Parses and verifies the JWT.
                .getBody() // Retrieves the JWT payload.
                .getSubject(); // Extracts the username from the payload.
    }
}
