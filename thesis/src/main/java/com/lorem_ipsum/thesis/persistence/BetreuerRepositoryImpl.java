package com.lorem_ipsum.thesis.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;

@Repository
public class BetreuerRepositoryImpl implements BetreuerRepository {

    private final BetreuerDbRepo db;

    public BetreuerRepositoryImpl(BetreuerDbRepo db) {
        this.db = db;
    }


    @Override
    public Optional<BetreuerProfile> findById(Integer id) {
        return db.findById(id).map(this::toBetreuerProfile);
    }

    @Override
    public Optional<BetreuerProfile> findByGithubID(String githubId) {
        return db.findBetreuerProfileByGithubID(githubId).map(this::toBetreuerProfile);

    }

    @Override
    public Optional<BetreuerProfile> findByEmail(String email) {
        return db.findBetreuerProfileByEmail(email).map(this::toBetreuerProfile);
    }

    @Override
    public List<BetreuerProfile> findByTag(String tag) {
        return db.findByTag(tag).stream().map(this::toBetreuerProfile).toList();
    }

    @Override
    public List<BetreuerProfile> findAll() {
        return db.findAll().stream().map(this::toBetreuerProfile).toList();
    }

    @Override
    public Optional<BetreuerProfile> findByName(String name) {
        return db.findBetreuerDtoByName(name).map(this::toBetreuerProfile);
    }

    @Override
    public void save(BetreuerProfile profile) {
        BetreuerDto betreuerDto = toBetreuerDto(profile);

        db.save(betreuerDto);
    }

    @Override
    public boolean existsByGithubLogin(String githubLogin) {
        return db.existsByGithubID(githubLogin);
    }

    private BetreuerProfile toBetreuerProfile(BetreuerDto betreuerDto) {
        return new BetreuerProfile(betreuerDto.id(), betreuerDto.githubID()
                , betreuerDto.name(), betreuerDto.email(), betreuerDto.tags());
    }

    private BetreuerDto toBetreuerDto(BetreuerProfile betreuerProfile) {
        return new BetreuerDto(betreuerProfile.getId(), betreuerProfile.getGithubID()
                , betreuerProfile.getName(), betreuerProfile.getEmail(), betreuerProfile.getTags());
    }
}
