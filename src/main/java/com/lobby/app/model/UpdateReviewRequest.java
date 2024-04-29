package com.lobby.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReviewRequest {
    Integer reviewId;
    String reviewText;
    String summary;
    Integer rating;
}
