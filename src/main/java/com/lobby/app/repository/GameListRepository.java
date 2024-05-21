package com.lobby.app.repository;

import com.lobby.app.model.GameList;
import com.lobby.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameListRepository extends JpaRepository<GameList, Integer> {
    List<GameList> findAllByUser(User user);
}
