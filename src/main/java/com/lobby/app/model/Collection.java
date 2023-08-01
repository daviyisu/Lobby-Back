package com.lobby.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "collection")
public class Collection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Long game_id;
}
