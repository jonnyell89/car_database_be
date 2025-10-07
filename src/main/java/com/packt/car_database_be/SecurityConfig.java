package com.packt.car_database_be;

import com.packt.car_database_be.service.UserDetailsServiceImplementation;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Fetches users from the userRepository and returns UserDetails for Spring Security.
    private final UserDetailsServiceImplementation userDetailsServiceImplementation;

    // Custom filter that reads JWT from Authorization header and sets Authentication.
    private final AuthenticationFilter authenticationFilter;

    // Intercepts unauthorised HTTP requests and gives a controlled and consistent response.
    private final AuthenticationEntryPointImplementation exceptionHandler;

    // Constructor dependency injection.
    public SecurityConfig(UserDetailsServiceImplementation userDetailsServiceImplementation, AuthenticationFilter authenticationFilter, AuthenticationEntryPointImplementation exceptionHandler) {
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
        this.authenticationFilter = authenticationFilter;
        this.exceptionHandler = exceptionHandler;
    }

    // Attempts to configure the AuthenticationManagerBuilder to use the userDetailsServiceImplementation and a BCrypt password encoder.
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder
                    .userDetailsService(userDetailsServiceImplementation) // Tells Spring Security to use userDetailsServiceImplementation to load users.
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

    // Builds and returns a SecurityFilterChain bean that defines all security rules for incoming HTTP requests.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Disables CSRF for a stateless REST API, typical for JWT.
        httpSecurity.csrf((csrf) -> csrf.disable())
                // No HTTP session in stateless JWT-based APIs.
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // This section defines permissions.
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        // Allows POST /login without authentication, necessary for JWT assignment.
                        .requestMatchers(HttpMethod.POST, "/login")
                        .permitAll()
                        // All other endpoints require authentication.
                        .anyRequest()
                        .authenticated())
                // Adds custom JWT-authentication filter before UsernamePasswordAuthenticationFilter so that JWT validation on non-login routes happens before Spring's default filter.
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Registers custom entry point for exception handling.
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(exceptionHandler));

        // Finalises configuration and builds the SecurityFilterChain object.
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
