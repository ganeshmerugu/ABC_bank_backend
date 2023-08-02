package com.ABC.Bank.ControllerTests;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ABC.Bank.Controller.AuthController;
import com.ABC.Bank.Data.LoginRequest;
import com.ABC.Bank.Entity.User;
import com.ABC.Bank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // Test data
    private User testUser;
    private final String testUserName = "testuser";
    private final String testPassword = "testpassword";

    @BeforeEach
    public void setUp() {
// Initialize test data before each test case
        testUser = new User();
        testUser.setUserName(testUserName);
        testUser.setPassword(testPassword);
    }

    @Test
    public void testLoginUser_Success() throws Exception {
// Mock the userService to return the testUser when getUserByUserName is called
        when(userService.getUserByUserName(testUserName)).thenReturn(testUser);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(testUserName);
        loginRequest.setPassword(testPassword);

// Perform the POST request with JSON data and expect a 202 ACCEPTED response
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
// Mock the userService to return null, simulating invalid credentials
        when(userService.getUserByUserName(testUserName)).thenReturn(null);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(testUserName);
        loginRequest.setPassword(testPassword);

// Perform the POST request with JSON data and expect a 400 BAD REQUEST response
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    // Utility method to convert an object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}