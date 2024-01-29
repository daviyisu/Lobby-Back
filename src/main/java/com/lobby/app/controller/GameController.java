package com.lobby.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobby.app.config.Key;
import com.lobby.app.model.Game;
import com.lobby.app.repository.GameRepository;
import com.lobby.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class GameController {

    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private static final String STEAM_API_BASE = "https://api.steampowered.com";

    private static final String IGDB_API_BASE = "https://api.igdb.com/v4/";

    private static final String STEAM_URL_BASE = "https://store.steampowered.com/app/";

    private static final String STEAM_API_KEY = Key.steamApiKey;

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

    /*
    Extract a list of Steam IDs games list given a Steam user ID
    */
    private List<Integer> getSteamAppIds(Long userSteamId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assert webClientBuilder != null;
        String jsonResponse = webClientBuilder.build()
                .get()
                .uri(STEAM_API_BASE + "/IPlayerService/GetOwnedGames/v0001/?key="
                        + STEAM_API_KEY
                        + "&steamid=" + userSteamId
                        + "&include_appinfo=true&format=json")
                .retrieve().bodyToMono(String.class).block();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<Integer> result = new ArrayList<>();
        JsonNode gamesArray = jsonNode.get("response").get("games");
        for (int i = 0; i < gamesArray.size(); i++) {
            result.add(gamesArray.get(i).get("appid").asInt());
        }
        return result;
    }

    /*
    Extract a list of the equivalent IGDB games IDs from a Steam IDs games list
    */
    private List<Integer> getIgdbGamesIdsFromSteam(List<Integer> steamAppIds) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assert webClientBuilder != null;
        StringBuilder requestBody = new StringBuilder().append("fields game; where url=\"")
                .append(STEAM_URL_BASE)
                .append(steamAppIds.get(0).toString())
                .append("\"");
        if (steamAppIds.size() > 1) {
            for (int i = 1; i < steamAppIds.size(); i++) {
                requestBody.append(" | url=\"")
                        .append(STEAM_URL_BASE)
                        .append(steamAppIds.get(i).toString())
                        .append("\"");
            }
        }
        requestBody.append("; limit 500;");
        String jsonResponse = webClientBuilder.build()
                .post()
                .uri(IGDB_API_BASE + "/websites")
                .header("Client-ID", Key.clientId)
                .header("Authorization", "Bearer " + Key.accessToken)
                .body(BodyInserters.fromValue(requestBody.toString()))
                .retrieve().bodyToMono(String.class).block();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < jsonNode.size(); i++) {
            result.add(jsonNode.get(i).get("game").asInt());
        }
        return result;
    }

    // TODO This endpoint needs revision
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Integer id) throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            return optionalGame.get();
        } else {
            throw new Exception();
        }
    }


    /*
    Returns a list of IGDB IDs games list given a Steam user ID
    */
    @GetMapping("/steamgames/{userSteamId}")
    public List<Integer> getSteamGames(@PathVariable Long userSteamId) throws JsonProcessingException {
        List<Integer> steamAppIds = this.getSteamAppIds(userSteamId);
        return this.getIgdbGamesIdsFromSteam(steamAppIds);
    }

//    @GetMapping("/getimage/{gameId}")
//    public String getCoverId(@PathVariable Long gameId) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        assert webClientBuilder != null;
//        String requestBody = "fields image_id; where game=" + gameId + ";" + " limit 500;";
//
//        String jsonResponse = webClientBuilder.build()
//                .post()
//                .uri(IGDB_API_BASE + "/covers")
//                .header("Client-ID", Key.clientId)
//                .header("Authorization", "Bearer " + Key.accessToken)
//                .body(BodyInserters.fromValue(requestBody))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
//
//        return "https://images.igdb.com/igdb/image/upload/t_1080p/" + jsonNode.get(0).get("image_id").asText() + ".jpg";
//    }


    /*
    ALGUN DIA USARE ESTO
    StringBuilder requestBody = new StringBuilder("fields name; where id=").append(igdbGamesIds.get(0));
        if (igdbGamesIds.size() > 1) {
            for (int i = 1; i < igdbGamesIds.size(); i++) {
                requestBody.append(" | id=").append(igdbGamesIds.get(i).toString());
            }
        }
        requestBody.append("; limit 500;");
        String jsonResponse = webClientBuilder.build()
                .post()
                .uri(this.igdbApiBase + "/games")
                .header("Client-ID", Key.clientId)
                .header("Authorization", "Bearer " + Key.accessToken)
                .body(BodyInserters.fromValue(requestBody.toString()))
                .retrieve().bodyToMono(String.class).block();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < jsonNode.size(); i++) {
            result.add(jsonNode.get(i).get("name").toString());
        }
        return result;
    * */
}
