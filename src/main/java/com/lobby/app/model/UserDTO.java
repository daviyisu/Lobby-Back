package com.lobby.app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String username;

    public UserDTO(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}

