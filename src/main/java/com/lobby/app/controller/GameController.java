package com.lobby.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;
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

    private final String igdbApiBase = "https://api.igdb.com/v4/";

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

    private List<Integer> getSteamAppIds(Long userSteamId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assert webClientBuilder != null;
        String jsonResponse = webClientBuilder.build()
                .get()
                .uri(this.steamApiBase + "/IPlayerService/GetOwnedGames/v0001/?key="
                        + this.steamApiKey
                        + "&steamid="+userSteamId
                        +"&include_appinfo=true&format=json")
                .retrieve().bodyToMono(String.class).block();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<Integer> result = new ArrayList<>();
        JsonNode gamesArray = jsonNode.get("response").get("games");
        for (int i=0; i<gamesArray.size(); i++) {
            result.add(gamesArray.get(i).get("appid").asInt());
        }
        return result;
    }

    private List<Integer> getIgdbGamesIdsFromSteam(List<Integer> steamAppIds) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assert webClientBuilder != null;
        StringBuilder requestBody = new StringBuilder("fields game; where url=\"https://store.steampowered.com/app/" + steamAppIds.get(0).toString() + "\"");
        if(steamAppIds.size() > 1) {
            for (int i=1; i<steamAppIds.size(); i++) {
                requestBody.append(" | url=\"https://store.steampowered.com/app/").append(steamAppIds.get(i).toString()).append("\"");
            }
        }
       requestBody.append("; limit 500;");
        String jsonResponse = webClientBuilder.build()
                .post()
                .uri(this.igdbApiBase + "/websites")
                .header("Client-ID", Key.clientId)
                .header("Authorization", "Bearer " + Key.accessToken)
                .body(BodyInserters.fromValue(requestBody.toString()))
                .retrieve().bodyToMono(String.class).block();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<Integer> result = new ArrayList<>();
        for(int i=0; i<jsonNode.size(); i++) {
            result.add(jsonNode.get(i).get("game").asInt());
        }
        return result;
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

    /*
    Extract all Steam appIds from a user Steam library
    */
    @GetMapping("/steamgames/{userSteamId}")
    public List<Integer> getSteamGames(@PathVariable Long userSteamId) throws JsonProcessingException {
        List<Integer> steamAppIds = this.getSteamAppIds(userSteamId);
        return this.getIgdbGamesIdsFromSteam(steamAppIds);
    }

}
