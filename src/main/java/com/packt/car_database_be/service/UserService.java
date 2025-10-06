package com.packt.car_database_be.service;

import com.packt.car_database_be.domain.User;
import com.packt.car_database_be.domain.UserRepository;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Connects application database with Spring Security authentication system.
@Service
public class UserService implements UserDetailsService {

    // Dependency injection. Must be initialised through the constructor.
    private final UserRepository userRepository;

    // When creating a UserService object, Spring injects an instance of the UserRepository.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Called with login attempt or with JWT verification.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Optional prevents error if user is not found in userRepository.
        Optional<User> user = userRepository.findByUsername(username);

        // Prepares a UserBuilder object, which is used to create a UserDetails object.
        UserBuilder builder = null;
        // Optional requires .isPresent() check.
        if (user.isPresent()) {
            // Retrieves the User from the Optional.
            User currentUser = user.get();
            // Starts constructing a UserDetails object.
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            // Adds the encoded password to compare with the password entered during login.
            builder.password(currentUser.getPassword());
            // Adds the user's role
            builder.roles(currentUser.getRole());
        } else {
            throw new UsernameNotFoundException("User not found in userRepository.");
        }
        // Converts the UserBuilder object into a UserDetails object and returns it to Spring Security.
        // This UserDetails object is what Spring uses to represent the authenticated user internally.
        return builder.build();
    }
}
