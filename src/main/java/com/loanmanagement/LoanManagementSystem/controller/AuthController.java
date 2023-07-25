package com.loanmanagement.LoanManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loanmanagement.LoanManagementSystem.exceptions.InvalidRepaymentAmountException;
import com.loanmanagement.LoanManagementSystem.exceptions.RepaymentNotFoundException;
import com.loanmanagement.LoanManagementSystem.exceptions.UnauthorizedAccessException;
import com.loanmanagement.LoanManagementSystem.exceptions.UsernameAlreadyExistsException;
import com.loanmanagement.LoanManagementSystem.models.User;
import com.loanmanagement.LoanManagementSystem.service.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String username, @RequestParam String password) {
        // Validate input parameters
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty.");
        }

        // Check if the username is already taken
        if (userService.isUsernameTaken(username)) {
            throw new UsernameAlreadyExistsException("Username already taken.");
        }

        User user = userService.registerUser(username, password, false);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        // This endpoint will be handled by Spring Security's form login
        // If login is successful, the user will be authenticated
        return ResponseEntity.ok("Login successful.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // This endpoint will be handled by Spring Security's logout
        // If logout is successful, the user will be logged out
        return ResponseEntity.ok("Logout successful.");
    }

    @ExceptionHandler({IllegalArgumentException.class, UsernameAlreadyExistsException.class})
    public ResponseEntity<String> handleBadRequest(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(RepaymentNotFoundException.class)
    public ResponseEntity<String> handleRepaymentNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRepaymentAmountException.class)
    public ResponseEntity<String> handleInvalidRepaymentAmount(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Add more exception handlers for other specific exceptions as needed...
}
