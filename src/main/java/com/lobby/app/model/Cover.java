package com.lobby.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Cover {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "image_id")
    private String imageId;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "game")
    private Integer game;
}

