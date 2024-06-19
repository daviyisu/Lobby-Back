package com.lobby.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobby.app.config.Key;
import com.lobby.app.exception.SteamPrivateAccountException;
import com.lobby.app.model.*;
import com.lobby.app.model.Collection;
import com.lobby.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepository gameRepository;

    private final WebsiteRepository websiteRepository;

    private final PlatformRepository platformRepository;

    private final CollectionRepository collectionRepository;

    private final CoverRepository coverRepository;

    private static final String STEAM_API_KEY = Key.steamApiKey;

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public GameController(GameRepository gameRepository,
                          WebsiteRepository websiteRepository,
                          PlatformRepository platformRepository,
                          WebClient.Builder webClientBuilder,
                          CollectionRepository collectionRepository,
                          CoverRepository coverRepository,
                          ObjectMapper objectMapper
    ) {
        this.gameRepository = gameRepository;
        this.websiteRepository = websiteRepository;
        this.platformRepository = platformRepository;
        this.webClient = webClientBuilder.build();
        this.collectionRepository = collectionRepository;
        this.coverRepository = coverRepository;
        this.objectMapper = objectMapper;
    }

    /*
    Extract a list of Steam IDs games list given a Steam user ID
    */
    private List<Integer> getSteamAppIds(String userSteamId) throws JsonProcessingException, SteamPrivateAccountException {
        String steamResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.steampowered.com")
                        .path("/IPlayerService/GetOwnedGames/v0001/")
                        .queryParam("key", STEAM_API_KEY)
                        .queryParam("steamid", userSteamId)
                        .queryParam("include_appinfo", true)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode jsonNode = objectMapper.readTree(steamResponse);
       JsonNode gamesArray = jsonNode.path("response").path("games");
        List<Integer> result = new ArrayList<>();
        if (gamesArray.isArray()) {
            for (JsonNode game : gamesArray) {
                result.add(game.path("appid").asInt());
            }
        }
        if (result.isEmpty()) {
            throw new SteamPrivateAccountException();
        }
        return result;
    }

    /*
    Search games in the database by it Steam app ID and adds it to the current user collection
    */
    private void getIgdbGamesIdsFromSteam(List<Integer> steamAppIds) {
        List<String> steamUrls = steamAppIds.stream().map(id -> "https://store.steampowered.com/app/" + id.toString()).toList();
        List<Integer> gamesIds = this.websiteRepository.findGamesByUrls(steamUrls);
        List<Collection> userCurrentCollection = this.collectionRepository.findAllByUser(User.getCurrentUser());
        Set<Integer> existingGameIds = userCurrentCollection.stream()
                .map(collection -> collection.getGame().getId())
                .collect(Collectors.toSet());
        List<Game> games = this.gameRepository.findAllByIdOrCategoryZero(gamesIds);
        List<Game> newGamesToAdd = games.stream()
                .filter(game -> !existingGameIds.contains(game.getId()))
                .toList();
        List<Collection> collections = newGamesToAdd.stream().map(game -> new Collection(User.getCurrentUser(), game, CollectionStatus.PLAYED)).toList();
        this.collectionRepository.saveAll(collections);
    }

    // TODO This endpoint needs revision
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Integer id) throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Cover cover = this.coverRepository.findCoverByGame(optionalGame.get().getId());
            optionalGame.get().setCoverImageId(
                    cover != null ? cover.getImageId() : null
            );
            return optionalGame.get();
        } else {
            throw new Exception();
        }
    }

    @GetMapping("usergames")
    public List<Game> getUserGames() {
        List<Game> result = new ArrayList<>();
        List<Collection> userGamesIds = this.collectionRepository.findAllByUser(User.getCurrentUser());
        if (!userGamesIds.isEmpty()) {
            for (Collection gameId: userGamesIds) {
                Optional<Game> optionalGame = this.gameRepository.findById(gameId.getGame().getId());
                optionalGame.ifPresent(game -> {
                    Cover cover = this.coverRepository.findCoverByGame(game.getId());
                    game.setCoverImageId(cover != null ? cover.getImageId() : null);
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
    Add all the steam games of this account (if public) to the user collection
    */
    @PostMapping("/steamgames")
    public void syncSteamGames(@RequestBody SteamSyncRequest userSteamId) throws JsonProcessingException, SteamPrivateAccountException {
        List<Integer> steamAppIds = this.getSteamAppIds(userSteamId.getSteamId());
        this.getIgdbGamesIdsFromSteam(steamAppIds);
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

    @GetMapping("/searchbyname")
    public List<Game> search(@RequestParam("query") String query) {
        List<Game> result = new ArrayList<>();
        List<Integer> categoriesToSearch = Arrays.asList(0, 8, 10);
        if (!query.isEmpty()) {
            result = this.gameRepository.findAllByNameContainingIgnoreCaseAndCategoryIn(query, categoriesToSearch);
        }
        if (result.size() >= 20) {
            return result.subList(0,20);
        } else {
            return result;
        }
    }

    @PostMapping("/addgame")
    public void addGame(@RequestBody GameStatusRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Optional<Collection> collection = this.collectionRepository.findByUserAndGameId(principal, request.getGameId());
        if (collection.isPresent()) {
            if (request.getStatus() == CollectionStatus.NOT_OWNED) {
                this.collectionRepository.delete(collection.get());
            } else {
                collection.get().setStatus(request.getStatus());
                this.collectionRepository.save(collection.get());
            }
        } else {
            Optional<Game> newGame = this.gameRepository.findById(request.getGameId());
            Collection new_collection = new Collection(principal, newGame.get(), request.getStatus());
            this.collectionRepository.save(new_collection);
        }

    }

    @GetMapping("/owns/{id}")
    public CollectionStatus hasGameCheck(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Optional<Collection> collection = this.collectionRepository.findByUserAndGameId(principal, id);
        if (collection.isPresent()) {
            return collection.get().getStatus();
        }
        else return CollectionStatus.NOT_OWNED;
    }

    @GetMapping("collectioncount")
    public Integer getCollectionCount() {
        return this.collectionRepository.countAllByUser(User.getCurrentUser());
    }

    @GetMapping("countbystatus/{status}")
    public Integer getCountByStatus(@PathVariable CollectionStatus status) {
        return this.collectionRepository.countAllByUserAndStatus(User.getCurrentUser(), status);
    }

    @GetMapping("recentgames")
    public List<Game> getRecentGames() {
        List<Game> gamesToReturn = this.gameRepository.findRecentAddedGames();
        if (!gamesToReturn.isEmpty()) {
            for (Game game : gamesToReturn) {
                Cover cover = this.coverRepository.findCoverByGame(game.getId());
                game.setCoverImageId(cover != null ? cover.getImageId() : null);
            }
        }
        return gamesToReturn;
    }
}
