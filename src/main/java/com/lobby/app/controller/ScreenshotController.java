package com.lobby.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/screenshots")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class ScreenshotController {
}
