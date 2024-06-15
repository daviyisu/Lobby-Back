package com.lobby.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Website {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "category")
    private Integer category;

    @Column(name = "url")
    private String url;

    @Column(name = "game")
    private Integer game;
}

