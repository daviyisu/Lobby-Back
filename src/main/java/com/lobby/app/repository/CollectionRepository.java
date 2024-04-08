package com.lobby.app.repository;

import com.lobby.app.model.Collection;
import com.lobby.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    List<Collection> findAllByUser(User user);
    Collection findByUserAndGameId(User user, Integer gameId);
}
