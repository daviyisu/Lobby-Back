package com.lobby.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Game {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "summary")
    private String summary;

    @Column(name = "storyline")
    private String storyline;

    @Column(name = "aggregated_rating")
    private String aggregatedRating;

    @Column(name = "parent_game")
    private Integer parentGame;

    @Column(name = "genres")
    private String genres;

    @Column(name = "first_release_date")
    private String firstReleaseDate;

    @Column(name = "screenshots")
    private String screenshots;

    @Column(name = "videos")
    private String videos;

    @Column(name = "cover")
    private Integer cover;

    @Column(name = "involved_companies")
    private String involvedCompanies;

    @Column(name = "artworks")
    private String artworks;

    public String getName() {
        return name;
    }
}
