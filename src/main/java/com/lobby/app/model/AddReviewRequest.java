package com.lobby.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddReviewRequest {
    Integer gameId;
    Integer rating;
    String review_text;
}
