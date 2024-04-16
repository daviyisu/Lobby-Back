package com.lobby.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    @Column(name = "review_text", length = 1000)
    private String review_text;

    @Column(name = "summary")
    private String summary;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "createdAt")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Review(User user, Game game, String review_text, String summary, Integer likes, Integer rating) {
        this.user = user;
        this.game = game;
        this.review_text = review_text;
        this.summary = summary;
        this.likes = likes;
        this.rating = rating;
    }
}
