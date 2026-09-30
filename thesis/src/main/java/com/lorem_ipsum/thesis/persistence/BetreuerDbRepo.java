package com.lorem_ipsum.thesis.persistence;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface BetreuerDbRepo extends CrudRepository<BetreuerDto, Integer> {

    Optional<BetreuerDto> findBetreuerProfileByGithubID(String githubID);

    @Query("SELECT * FROM betreuer_profile WHERE email = :email")
    Optional<BetreuerDto> findBetreuerProfileByEmail(String email);


    @Query("""
              SELECT * FROM betreuer_profile bp
              WHERE EXISTS (
                SELECT 1
                FROM unnest(bp.tags) t
                WHERE lower(t) = lower(:tag)
              )
            """)
    List<BetreuerDto> findByTag(@Param("tag") String tag);

    List<BetreuerDto> findAll();

    Optional<BetreuerDto> findBetreuerDtoByName(String name);

    @Query("SELECT EXISTS (SELECT 1 FROM betreuer_profile WHERE github_id = :githubID)")
    boolean existsByGithubID(@Param("githubID") String githubID);
}
