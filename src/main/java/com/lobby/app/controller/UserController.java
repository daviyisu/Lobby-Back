package com.lobby.app.controller;


import com.lobby.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.lobby.app.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        Optional<User> optionalUser;
        assert userRepository != null;
        optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
        }
    }

    @GetMapping("/name_ping")
    public String private_ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getPrincipal().toString();
        return "Ping for " + name;
    }

    @GetMapping("/getbyusername/{username}")
    public User getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser;
        assert userRepository != null;
        optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }

    @GetMapping("/current_user")
    public User getCurrentUser() {
        return User.getCurrentUser();
    }
}