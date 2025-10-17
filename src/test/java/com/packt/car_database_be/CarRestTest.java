package com.packt.car_database_be;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// This test mimics an HTTP request inside the JVM using Spring's test framework, without starting a real web server.

// Loads the full Spring Boot application context for integration testing.
@SpringBootTest
// Automatically sets up MockMvc for testing web layers.
@AutoConfigureMockMvc
public class CarRestTest {

    // Injects a MockMvc instance for simulating HTTP requests.
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticationPlusJsonWebToken() throws Exception {

        // Performs a login request and captures the result.
        MvcResult result = this.mockMvc
                // Sends a simulated POST request to /login.
                .perform(post("/login")
                        // Sets the request content type.
                        .contentType("application/json")
                        // Provides a JSON body for the request.
                        .content("{\"username\":\"admin\", \"password\":\"admin\"}"))
                // Prints request/response details to the console.
                .andDo(print())
                // Asserts that the HTTP status code is 200 OK.
                .andExpect(status().isOk())
                // Returns the response.
                .andReturn();

        // Extracts the response Authorization header as a String.
        String responseHeader = result.getResponse().getHeader("Authorization");
        System.out.println("Authorization header: " + responseHeader);

        // Checks that the response Authorization header contains a JSON Web Token.
        assertThat(responseHeader)
                .isNotNull()
                .startsWith("Bearer");
    }
}
