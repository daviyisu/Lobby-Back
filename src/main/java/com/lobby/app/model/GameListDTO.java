package com.lobby.app.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameListDTO {
    private Integer id;
    private String name;
    private UserDTO user;
    private List<Game> games;

    public GameListDTO(Integer id, String name, UserDTO user, List<Game> games) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.games = games;
    }
}

