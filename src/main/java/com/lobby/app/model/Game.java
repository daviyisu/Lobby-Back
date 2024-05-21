package com.lobby.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Game {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "storyline", columnDefinition = "TEXT")
    private String storyline;

    @Column(name = "aggregated_rating")
    private Integer aggregatedRating;

    @Column(name = "parent_game")
    private Integer parentGame;

    @Column(name = "genres")
    private int[] genres;

    @Column(name = "platforms")
    private int[] platforms;

    @Column(name = "first_release_date")
    private Date firstReleaseDate;

    @Column(name = "screenshots")
    private int[] screenshots;

    @Column(name = "videos")
    private int[] videos;

    @Column(name = "cover")
    private Integer cover;

    @Column(name = "involved_companies")
    private int[] involvedCompanies;

    @Column(name = "artworks")
    private int[] artworks;

    @Column(name = "category")
    private int category;

    private String coverImageId;
}
