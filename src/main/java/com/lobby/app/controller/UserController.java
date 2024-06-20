package com.lobby.app.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobby.app.config.Key;
import com.lobby.app.exception.SteamAccountDoesNotExistsException;
import com.lobby.app.exception.SteamPrivateAccountException;
import com.lobby.app.model.SteamUser;
import com.lobby.app.model.UpdateAvatarRequest;
import com.lobby.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.lobby.app.repository.UserRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class UserController {

    private static final String STEAM_API_KEY = Key.steamApiKey;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          WebClient.Builder webClientBuilder,
                          ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
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

    @GetMapping("/steam_data/{steamUserId}")
    public SteamUser getSteamUserData(@PathVariable String steamUserId) throws JsonProcessingException, SteamPrivateAccountException, SteamAccountDoesNotExistsException {
        String steamResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.steampowered.com")
                        .path("/ISteamUser/GetPlayerSummaries/v0002/")
                        .queryParam("key", STEAM_API_KEY)
                        .queryParam("steamids", steamUserId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode jsonNode = objectMapper.readTree(steamResponse);
        JsonNode userDataJson = jsonNode.path("response").path("players");

        if (userDataJson.isEmpty()) {
            throw new SteamAccountDoesNotExistsException();
        }

        if (userDataJson.path(0).path("communityvisibilitystate").asInt() == -1) {
            throw new SteamPrivateAccountException();
        }

        return new SteamUser(
                userDataJson.path(0).path("steamid").asText(),
                userDataJson.path(0).path("personaname").asText(),
                userDataJson.path(0).path("avatarmedium").asText(),
                userDataJson.path(0).path("communityvisibilitystate").asInt()
        );
    }

    @PostMapping("/avatar")
    public void uploadAvatar(@RequestBody UpdateAvatarRequest request) {
        User user = User.getCurrentUser();
        user.setAvatar_url(request.getAvatarUrl());
        this.userRepository.save(user);
    }
}