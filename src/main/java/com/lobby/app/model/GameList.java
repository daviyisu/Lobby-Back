package com.lobby.app.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "game_list")
public class GameList {

    @Id
    @Column(name= "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "game_ids_on_list", joinColumns = @JoinColumn(name = "list_id"))
    @Column(name = "game_id")
    private List<Integer> games_ids;
}
