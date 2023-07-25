package com.loanmanagement.LoanManagementSystem.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.loanmanagement.LoanManagementSystem.models.User;
import com.loanmanagement.LoanManagementSystem.repository.UserRepository;
import com.loanmanagement.LoanManagementSystem.securityconfig.SecurityConfig;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public User registerUser(String username, String password, boolean isAdmin) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(SecurityConfig.passwordEncoder().encode(password));
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }

    public Boolean isUsernameTaken(String username) {
       User user = userRepository.findByUsername(username);
       if (user == null)
       return false;
       return true;
    }
}

