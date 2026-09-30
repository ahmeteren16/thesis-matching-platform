package com.lorem_ipsum.thesis.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InformationsDateiDbRepo extends CrudRepository<InformationsDateiDto, Integer> {
    
    List<InformationsDateiDto> findByBetreuerId(Integer betreuerId);
}