package com.project.service;

import com.project.domain.Place;
import com.project.domain.dto.PlaceDto;

import java.util.List;
import java.util.Optional;

public interface PlaceService {

    List<Place> findAll();

    List<Place> findAllByNameContaining(String word);

    Optional<Place> findById(Long id);

    Optional<Place> add(PlaceDto placeDto);

    Optional<Place> edit(Long id, PlaceDto placeDto);

    void deleteById(Long id);
}
