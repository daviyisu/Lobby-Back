package com.lobby.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobby.app.config.Key;
import com.lobby.app.model.Collection;
import com.lobby.app.model.Game;
import com.lobby.app.model.Platform;
import com.lobby.app.model.User;
import com.lobby.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class GameController {

    private final GameRepository gameRepository;

    private final PlatformRepository platformRepository;

    private final CollectionRepository collectionRepository;

    private final UserRepository userRepository;

    private final CoverRepository coverRepository;

    private static final String STEAM_API_BASE = "https://api.steampowered.com";

    private static final String IGDB_API_BASE = "https://api.igdb.com/v4/";

    private static final String STEAM_URL_BASE = "https://store.steampowered.com/app/";

    private static final String STEAM_API_KEY = Key.steamApiKey;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GameController(GameRepository gameRepository,
                          UserRepository userRepository,
                          PlatformRepository platformRepository,
                          WebClient.Builder webClientBuilder,
                          CollectionRepository collectionRepository,
                          CoverRepository coverRepository
    ) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.platformRepository = platformRepository;
        this.webClientBuilder = webClientBuilder;
        this.collectionRepository = collectionRepository;
        this.coverRepository = coverRepository;
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
            optionalGame.get().setCoverImageId(
                    this.coverRepository.findCoverByGame(optionalGame.get().getId()).getImageId()
            );
            return optionalGame.get();
        } else {
            throw new Exception();
        }
    }

    @GetMapping("usergames")
    public List<Game> getUserGames() throws Exception {
        List<Game> result = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Optional<User> user = this.userRepository.findById(principal.getId());
        List<Collection> userGamesIds = this.collectionRepository.findAllByUser(user.get());
        if (!userGamesIds.isEmpty()) {
            for (Collection gameId: userGamesIds) {
                Optional<Game> optionalGame = this.gameRepository.findById(gameId.getGame().getId());
                optionalGame.ifPresent(game -> {
                    game.setCoverImageId(this.coverRepository.findCoverByGame(game.getId()).getImageId());
                    result.add(game);
                });
            }
        }
        return result;
    }

    @GetMapping("/private_ping")
    public String private_ping() {
       return "private pong";
    }

    /*
    Returns a list of IGDB IDs games list given a Steam user ID
    */
    @GetMapping("/steamgames/{userSteamId}")
    public List<Integer> getSteamGames(@PathVariable Long userSteamId) throws JsonProcessingException {
        List<Integer> steamAppIds = this.getSteamAppIds(userSteamId);
        return this.getIgdbGamesIdsFromSteam(steamAppIds);
    }

    /*
    Returns a list of platforms names given a game ID
    */
    @GetMapping("/{id}/platforms")
    public List<String> getGamePlatforms(@PathVariable Integer id) {
        List<String> result = new ArrayList<>();
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game foundGame = optionalGame.get();
            if (foundGame.getPlatforms() != null) {
                for (Integer platformId : foundGame.getPlatforms()) {
                    Optional<Platform> optionalPlatform = this.platformRepository.findById(platformId);
                    optionalPlatform.ifPresent(platform -> result.add(platform.getName()));
                }
            }
        }
        return result;
    }
}
