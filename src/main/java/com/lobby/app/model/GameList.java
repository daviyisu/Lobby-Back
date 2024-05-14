package com.lobby.app.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Setter
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

    @ElementCollection
    @CollectionTable(name = "game_ids_on_list", joinColumns = @JoinColumn(name = "list_id"))
    @Column(name = "game_id")
    private List<Integer> games_ids;

    public GameList(String name, User user, List<Integer> games_ids) {
        this.name = name;
        this.user = user;
        this.games_ids = games_ids;
    }
}
