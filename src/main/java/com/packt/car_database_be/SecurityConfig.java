package com.packt.car_database_be;

import com.packt.car_database_be.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Application-specific service that implements UserDetailsService.
    // Fetches users from the userRepository and returns UserDetails for Spring Security.
    private final UserService userService;

    // Custom filter that reads JWT from Authorization header and sets Authentication.
    private final AuthenticationFilter authenticationFilter;

    // Constructor dependency injection.
    public SecurityConfig(UserService userService, AuthenticationFilter authenticationFilter) {
        this.userService = userService;
        this.authenticationFilter = authenticationFilter;
    }

    // Attempts to configure the AuthenticationManagerBuilder to use the userService and a BCrypt password encoder.
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder
                    .userDetailsService(userService) // Tells Spring Security to use userService to load users.
                    .passwordEncoder(new BCryptPasswordEncoder()); // Tells Spring Security that passwords are BCrypt-hashed.
    }

    // Exposes PasswordEncoder as a bean.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Exposes AuthenticationManager from AuthenticationConfiguration as a bean.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Builds and returns a SecurityFilterChain bean that describes HTTP security rules.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf((csrf) -> csrf.disable()) // Disables CSRF for a stateless REST API, typical for JWT.
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No HTTP session.
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/login")
                        .permitAll() // Allows POST /login without authentication.
                        .anyRequest()
                        .authenticated()) // Everything else requires authentication.
                // Adds JWT-authentication before UsernamePasswordAuthenticationFilter so that JWT validation on non-login routes happens early in the chain.
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
