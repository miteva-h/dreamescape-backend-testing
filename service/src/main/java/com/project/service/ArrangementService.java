package com.project.service;

import com.project.domain.Arrangement;
import com.project.domain.dto.ArrangementDto;
import com.project.domain.relations.ArrangementInShoppingCart;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArrangementService {

    List<Arrangement> findAllByAccommodation(Long accommodationId);

    List<LocalDate> getAllDatesForAccommodation(Long accommodationId);

    Optional<Arrangement> add(ArrangementDto arrangementDto);

    List<ArrangementInShoppingCart> getAllArrangementsForUser(String username);

    Optional<Arrangement> findById(Long id);

    void deleteById(Long id);

}
