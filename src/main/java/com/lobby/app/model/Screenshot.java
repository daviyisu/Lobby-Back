package com.lobby.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Screenshot {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "alpha_channel")
    private String alphaChannel;

    @Column(name = "animated")
    private String animated;

    @Column(name = "game")
    private Integer game;

    @Column(name = "checksum")
    private String checksum;


}
