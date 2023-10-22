package com.lobby.app.controller;

import com.lobby.app.config.Key;
import com.lobby.app.model.Game;
import com.lobby.app.repository.GameRepository;
import com.lobby.app.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor(force = true)
@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class GameController {

    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final String steamApiBase = "https://api.steampowered.com";

    private final String steamUrlBase = "https://store.steampowered.com/app/";

    private final String steamApiKey = Key.steamApiKey;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GameController(GameRepository gameRepository,
                          UserRepository userRepository,
                          WebClient.Builder webClientBuilder
    ) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.webClientBuilder = webClientBuilder;
    }

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

    @PostMapping("/addgame")
    public ResponseEntity<Game> addUser(@RequestBody Game newGame){
        assert this.gameRepository != null;
        this.gameRepository.save(newGame);
        return ResponseEntity.ok(newGame);
    }

    @GetMapping("/steamgames/{userSteamId}")
    public String getSteamGames(@PathVariable Long userSteamId) {
        assert webClientBuilder != null;
        return webClientBuilder.build()
                .get()
                .uri(this.steamApiBase + "/IPlayerService/GetOwnedGames/v0001/?key="
                        + this.steamApiKey
                        + "&steamid="+userSteamId
                        +"&include_appinfo=true&format=json")
                .retrieve().bodyToMono(String.class).block();
    }

}
