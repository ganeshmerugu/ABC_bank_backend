package com.ABC.Bank.ControllerTests;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ABC.Bank.Controller.RegistrationController;
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

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

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
    public void testRegisterUser_Success() throws Exception {
      // Mock the userService to return the testUser when saveUser is called
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

// Perform the POST request with JSON data and expect a 201 CREATED response
        mockMvc.perform(post("/api/registration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUser)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterUser_Failure() throws Exception {
      // Mock the userService to return null, simulating a failure in user registration
        when(userService.saveUser(any(User.class))).thenReturn(null);

     // Perform the POST request with JSON data and expect a 500 INTERNAL SERVER ERROR response
        mockMvc.perform(post("/api/registration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUser)))
                .andExpect(status().isInternalServerError());
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