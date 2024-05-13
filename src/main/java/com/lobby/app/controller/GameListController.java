package com.lobby.app.controller;

import com.lobby.app.model.InputGameList;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game_list")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class GameListController {

    @PostMapping("/new")
    public void createList(@RequestBody InputGameList gameList) {
        System.out.println(gameList);
    }
}
