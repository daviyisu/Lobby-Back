package com.lobby.app.controller;

import com.lobby.app.model.GameList;
import com.lobby.app.model.InputGameList;
import com.lobby.app.model.User;
import com.lobby.app.repository.GameListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game_list")
public class GameListController {

    private final GameListRepository gameListRepository;

    @Autowired
    public GameListController(GameListRepository gameListRepository) {
        this.gameListRepository = gameListRepository;
    }

    @PostMapping("/new")
    public void createList(@RequestBody InputGameList requestList) {
        GameList newGameList = new GameList(requestList.getName(), User.getCurrentUser(), requestList.getIdList());
        this.gameListRepository.save(newGameList);
    }
}
