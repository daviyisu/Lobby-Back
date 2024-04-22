package com.lobby.app.controller;

import com.lobby.app.model.*;
import com.lobby.app.repository.GameRepository;
import com.lobby.app.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class ReviewController {

    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewController(ReviewRepository reviewRepository, GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/addreview")
    public void addReview(@RequestBody AddReviewRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Optional<Game> game = this.gameRepository.findById(request.getGameId());
        if (game.isPresent()) {
            Review newReview = new Review(principal, game.get(), request.getReview_text(), request.getSummary(), 0, request.getRating());
            this.reviewRepository.save(newReview);
        } else {
            throw new Exception();
        }
    }

    @GetMapping("/ofgame/{gameId}")
    public List<ReviewDTO> getReviewsByGame(@PathVariable Integer gameId) {
        List<ReviewDTO> reviews = new ArrayList<>();
        if (this.gameRepository.findById(gameId).isPresent()) {
            reviews = this.reviewRepository.findReviewsByGameId(gameId);
        }
        return reviews;
    }

    @DeleteMapping("{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId) {
        this.reviewRepository.deleteById(reviewId);
    }
}
