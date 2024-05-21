package com.lobby.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "game_list")
public class GameList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_list_games",
            joinColumns = @JoinColumn(name = "game_list_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> games;

    public GameList(String name, User user,  List<Game> games_ids) {
        this.name = name;
        this.user = user;
        this.games = games_ids;
    }
}
