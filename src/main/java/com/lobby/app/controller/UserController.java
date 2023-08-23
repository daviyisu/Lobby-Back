package com.lobby.app.controller;


import com.lobby.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lobby.app.repository.UserRepository;

import java.util.Optional;

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

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        User user_to_return = new User();
        Optional<User> optionalUser;
        optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            user_to_return = optionalUser.get();
        }
        return user_to_return;
    }

//    @GetMapping("/{id}")
//    public User getUserByUsername(@PathVariable Long id){
//
//    }

}
