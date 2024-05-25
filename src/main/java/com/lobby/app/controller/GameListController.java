package com.lobby.app.controller;

import com.lobby.app.model.*;
import com.lobby.app.repository.CoverRepository;
import com.lobby.app.repository.GameListRepository;
import com.lobby.app.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game_list")
public class GameListController {

    private final GameListRepository gameListRepository;
    private final GameRepository gameRepository;
    private final CoverRepository coverRepository;

    @Autowired
    public GameListController(GameListRepository gameListRepository, CoverRepository coverRepository, GameRepository gameRepository) {
        this.gameListRepository = gameListRepository;
        this.coverRepository = coverRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/{id}")
    public GameListDTO findById(@PathVariable Integer id) {
        GameList listToReturn = gameListRepository.findById(id).orElseThrow();
        return convertToDTO(setGamesCovers(listToReturn));
    }

    @PostMapping("/new")
    public GameListDTO createList(@RequestBody InputGameList requestList) {
        GameList newGameList = new GameList(requestList.getName(), User.getCurrentUser(), requestList.getGames());
        this.gameListRepository.save(newGameList);
        return convertToDTO(setGamesCovers(newGameList));
    }

    @GetMapping("/from_user")
    public List<GameListDTO> getListFromUser() {
        List<GameList> gameLists = gameListRepository.findAllByUser(User.getCurrentUser());
        return gameLists.stream()
                .map(this::setGamesCovers)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PatchMapping("/add_to_list")
    public GameListDTO addGameToList(@RequestBody AddGameToListRequest request) {
        GameList gameListToUpdate = this.gameListRepository.findById(request.getListId()).orElseThrow();
        Game gameToAdd = this.gameRepository.findById(request.getGameId()).orElseThrow();
        gameListToUpdate.getGames().add(gameToAdd);
        this.gameListRepository.save(gameListToUpdate);
        return this.convertToDTO(setGamesCovers(gameListToUpdate));
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
