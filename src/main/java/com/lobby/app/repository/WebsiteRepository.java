package com.lobby.app.repository;

import com.lobby.app.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Integer> {

    @Query("SELECT w.game FROM Website w WHERE w.url IN :urls")
    List<Integer> findGamesByUrls(@Param("urls") List<String> urls);
}
