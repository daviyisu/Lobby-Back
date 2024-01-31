package com.lobby.app.repository;

import com.lobby.app.model.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot, Integer> {
    List<Screenshot> findAllByGame(Integer game);
}
