package com.lobby.app.repository;

import com.lobby.app.model.Review;
import com.lobby.app.model.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT new com.lobby.app.model.ReviewDTO(r.id, r.user.id, r.game.id, r.review_text, r.summary, r.likes, r.rating, r.createdAt, r.writtenBy)" +
            "FROM Review r WHERE r.game.id = :gameId")
    List<ReviewDTO> findReviewsByGameId(@Param("gameId") Integer gameId);
}
