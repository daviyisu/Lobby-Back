package com.lobby.app.repository;

import com.lobby.app.model.Cover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverRepository extends JpaRepository<Cover, Integer> {
    Cover findCoverByGame(Integer game);
}
