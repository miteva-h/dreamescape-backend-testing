package com.project.service;

import com.project.domain.Accommodation;
import com.project.domain.dto.AccommodationDto;

import java.util.List;
import java.util.Optional;

public interface AccommodationService {

    List<Accommodation> findAll();

    List<Accommodation> findAllByPlace(Long placeId);

    Optional<Accommodation> findById(Long id);

    Optional<Accommodation> add(AccommodationDto accommodationDto);

    Optional<Accommodation> edit(Long id, AccommodationDto accommodationDto);

    void deleteById(Long id);
}
