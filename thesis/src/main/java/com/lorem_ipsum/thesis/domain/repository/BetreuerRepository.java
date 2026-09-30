package com.lorem_ipsum.thesis.domain.repository;

import java.util.Collection;
import java.util.Optional;

import com.lorem_ipsum.thesis.domain.BetreuerProfile;


public interface BetreuerRepository {
    Optional<BetreuerProfile> findById(Integer id);

    Optional<BetreuerProfile> findByGithubID(String githubId);

    Optional<BetreuerProfile> findByEmail(String email);

    Collection<BetreuerProfile> findByTag(String tag);

    Collection<BetreuerProfile> findAll();

    Optional<BetreuerProfile> findByName(String name);

    void save(BetreuerProfile profile);

    boolean existsByGithubLogin(String githubLogin);


}
