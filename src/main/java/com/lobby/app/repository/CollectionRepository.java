package com.lobby.app.repository;

import com.lobby.app.model.Collection;
import com.lobby.app.model.CollectionStatus;
import com.lobby.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    List<Collection> findAllByUser(User user);
    Optional<Collection> findByUserAndGameId(User user, Integer gameId);
    Integer countAllByUser(User user);
    Integer countAllByUserAndStatus(User user, CollectionStatus status);
}
