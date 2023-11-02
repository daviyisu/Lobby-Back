package com.lobby.app.controller;


import com.lobby.app.model.Game;
import com.lobby.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lobby.app.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor   // Lombok's annotations to generate boiler code
@NoArgsConstructor(force = true)

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        Optional<User> optionalUser;
        assert userRepository != null;
        optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/getbyusername/{username}")
    public User getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser;
        assert userRepository != null;
        optionalUser = userRepository.findUserByUsername(username);
        User userToReturn = new User();
        if (optionalUser.isPresent()) {
            userToReturn = optionalUser.get();
        }
        return userToReturn;
    }

    @GetMapping("/{userId}/games/all")
    public Set<Game> getAllGamesOwned(@PathVariable Long userId){
        User userToReturn = getUserById(userId);
        return userToReturn.getGamesOwned();
    }

    @PostMapping("/adduser")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        assert this.userRepository != null;
        if (this.userRepository.findUserByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        this.userRepository.save(newUser);
        return ResponseEntity.ok(newUser);
    }



}
