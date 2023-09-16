package com.lobby.app.controller;

import com.lobby.app.model.Game;
import com.lobby.app.repository.GameRepository;
import com.lobby.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor   // Lombok's annotations to generate boiler code
@NoArgsConstructor(force = true)

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class GameController {

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id){
        Game game_to_return = new Game();
        Optional<Game> optionalGame;
        assert gameRepository != null;
        optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()){
            game_to_return = optionalGame.get();
        }
        return game_to_return;
    }

}
