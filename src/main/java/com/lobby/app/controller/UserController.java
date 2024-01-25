package com.lobby.app.controller;


import com.lobby.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.lobby.app.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@RequestMapping("/users")
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

    @GetMapping("/getbyusername/{username}")
    public User getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser;
        assert userRepository != null;
        optionalUser = userRepository.findUserByUsername(username);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }

//    @GetMapping("/{userId}/games/all")
//    public Set<Game> getAllGamesOwned(@PathVariable Long userId){
//        User userToReturn = getUserById(userId);
//        return userToReturn.getGamesOwned();
//    }

//    @PostMapping("/adduser")
//    public ResponseEntity<User> addUser(@RequestBody User newUser) {
//        assert this.userRepository != null;
//        if (this.userRepository.findUserByUsername(newUser.getUsername()).isPresent()) {
//            return ResponseEntity.badRequest().build();
//        }
//        this.userRepository.save(newUser);
//        return ResponseEntity.ok(newUser);
//    }
}