package com.lobby.app.repository;

import com.lobby.app.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findAllByNameContainingIgnoreCaseAndCategory(String name, Integer category);
}
