package com.lorem_ipsum.thesis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BetreuerService {

    private final BetreuerRepository repository;

    public BetreuerService(BetreuerRepository repository) {
        this.repository = repository;
    }

    public List<BetreuerProfile> getAllBetreuer() {
        List<BetreuerProfile> list = repository.findAll().stream().toList();

            return list;

        }



    public BetreuerProfile findByGithubId(String githubId) {
        return repository.findByGithubID(githubId).orElseThrow(NichtVorhandenException::new);

    }

    public BetreuerProfile findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(NichtVorhandenException::new);

    }

    public List<BetreuerProfile> findByTag(String tag) {
        return repository.findByTag(tag).stream().toList();


    }

    public BetreuerProfile findByName(String name) {
        return repository.findByName(name).orElseThrow(NichtVorhandenException::new);
    }

    public BetreuerProfile findById(Integer id) {
        return repository.findById(id).orElseThrow(NichtVorhandenException::new);
    }

    @Transactional
    public void save(BetreuerProfile betreuerProfile) {
        repository.save(betreuerProfile);
    }
}
