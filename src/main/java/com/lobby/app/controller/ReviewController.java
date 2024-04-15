package com.lobby.app.controller;

import com.lobby.app.model.AddReviewRequest;
import com.lobby.app.model.Game;
import com.lobby.app.model.Review;
import com.lobby.app.model.User;
import com.lobby.app.repository.GameRepository;
import com.lobby.app.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
            Review newReview = new Review(principal, game.get(), request.getReview_text(), 0, request.getRating());
            this.reviewRepository.save(newReview);
        } else {
            throw new Exception();
        }
    }
}
