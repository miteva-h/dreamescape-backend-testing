package com.project.service;

import com.project.domain.PhotoForPlace;
import com.project.domain.dto.PhotoForPlaceDto;

import java.util.List;
import java.util.Optional;

public interface PhotoForPlaceService {

    List<PhotoForPlace> findAll(Long placeId);

    Optional<PhotoForPlace> findById(Long id);

    Optional<PhotoForPlace> add(Long placeId, PhotoForPlaceDto photoForPlaceDto);

    Optional<PhotoForPlace> edit(Long id, PhotoForPlaceDto photoForPlaceDto);

    void deleteById(Long id);
}
