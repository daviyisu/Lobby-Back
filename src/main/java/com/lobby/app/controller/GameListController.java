package com.lobby.app.controller;

import com.lobby.app.model.*;
import com.lobby.app.repository.CoverRepository;
import com.lobby.app.repository.GameListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game_list")
public class GameListController {

    private final GameListRepository gameListRepository;
    private final CoverRepository coverRepository;

    @Autowired
    public GameListController(GameListRepository gameListRepository, CoverRepository coverRepository) {
        this.gameListRepository = gameListRepository;
        this.coverRepository = coverRepository;
    }

    @GetMapping("/{id}")
    public GameList findById(@PathVariable Integer id) {
        return gameListRepository.findById(id).orElse(null);
    }

    @PostMapping("/new")
    public void createList(@RequestBody InputGameList requestList) {
        GameList newGameList = new GameList(requestList.getName(), User.getCurrentUser(), requestList.getGames());
        this.gameListRepository.save(newGameList);
    }

    @GetMapping("/from_user")
    public List<GameListDTO> getListFromUser() {
        List<GameList> gameLists = gameListRepository.findAllByUser(User.getCurrentUser());
        return gameLists.stream()
                .map(this::setGamesCovers)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GameListDTO convertToDTO(GameList gameList) {
        UserDTO userDTO = new UserDTO(gameList.getUser().getId(), gameList.getUser().getUsername());
        return new GameListDTO(gameList.getId(), gameList.getName(), userDTO, gameList.getGames());
    }

    private GameList setGamesCovers(GameList gameList) {
        for (Game game : gameList.getGames()) {
            game.setCoverImageId(
                    this.coverRepository.findCoverByGame(game.getId()).getImageId()
            );
        }
        return gameList;
    }
}
