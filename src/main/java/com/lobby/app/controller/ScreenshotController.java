package com.lobby.app.controller;

import com.lobby.app.model.Screenshot;
import com.lobby.app.repository.ScreenshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screenshots")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class ScreenshotController {

    private final ScreenshotRepository screenshotRepository;

    @Autowired
    public ScreenshotController(ScreenshotRepository screenshotRepository) {
        this.screenshotRepository = screenshotRepository;
    }

    @GetMapping("/{gameId}")
    public List<Screenshot> getGameScreenshots(@PathVariable Integer gameId) {
        return screenshotRepository.findAllByGame(gameId);
    }
}
