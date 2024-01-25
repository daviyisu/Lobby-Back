package com.lobby.app.model;

import jakarta.persistence.*;


import java.io.Serializable;


@Entity
@Table(name = "\"user\"")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private String username;
    private Long steamId;



    public User(String username) {
        this.username = username;
    }

    public User() {}
}

