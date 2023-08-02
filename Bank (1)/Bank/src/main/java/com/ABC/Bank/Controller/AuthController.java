package com.ABC.Bank.Controller;

import com.ABC.Bank.Data.LoginRequest;
import com.ABC.Bank.Entity.User;
import com.ABC.Bank.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for user login
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200/login")
    public ResponseEntity<LoginRequest> loginUser(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
// Retrieve the user from the database based on the provided userName
        User user = userService.getUserByUserName(loginRequest.getUserName());

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

// Authentication successful
        request.getSession().setAttribute("username",loginRequest.getUserName());

        return new ResponseEntity<>(loginRequest,HttpStatus.ACCEPTED);

    }


// Other endpoints for authentication-related operations if needed
}