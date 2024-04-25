package com.lobby.app.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class ReviewDTO {
    private final Integer id;
    private final Integer userId;
    private final Integer gameId;
    private final String review_text;
    private final String summary;
    private final Integer likes;
    private final Integer rating;
    private final Date createdAt;
    private final String writtenBy;

    public ReviewDTO(Integer id, Integer userId, Integer gameId, String reviewText, String summary, Integer likes, Integer rating, Date createdAt, String writtenBy) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.review_text = reviewText;
        this.summary = summary;
        this.likes = likes;
        this.rating = rating;
        this.createdAt = createdAt;
        this.writtenBy = writtenBy;
    }
}
