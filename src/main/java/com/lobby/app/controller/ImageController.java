package com.lobby.app.controller;

import com.lobby.app.model.Screenshot;
import com.lobby.app.repository.ScreenshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class ImageController {

    private final ScreenshotRepository screenshotRepository;

    @Autowired
    public ImageController(ScreenshotRepository screenshotRepository) {
        this.screenshotRepository = screenshotRepository;
    }

    @GetMapping("/screenshot/{gameId}")
    public List<Screenshot> getGameScreenshots(@PathVariable Integer gameId) {
        return screenshotRepository.findAllByGame(gameId);
    }
}
