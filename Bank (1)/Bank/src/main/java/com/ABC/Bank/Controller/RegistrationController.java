package com.ABC.Bank.Controller;

import com.ABC.Bank.Entity.User;
import com.ABC.Bank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for user registration
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200/signup")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
// Perform any validation checks on user data if needed

// Save the user to the database
        User savedUser = userService.saveUser(user);

        if (savedUser != null) {
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// Other endpoints for registration-related operations if needed
}


