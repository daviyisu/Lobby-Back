package com.lobby.app.repository;

import com.lobby.app.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findAllByNameContainingIgnoreCaseAndCategoryIn(String name, List<Integer> categories);

    @Query("SELECT g FROM Game g WHERE g.createdAt >= CURRENT_DATE - 7 day ")
    List<Game> findRecentAddedGames();

    @Query("SELECT g FROM Game g WHERE g.id IN :gamesIds AND g.category = 0")
    List<Game> findAllByIdOrCategoryZero(@Param("gamesIds") List<Integer> gamesIds);
}
