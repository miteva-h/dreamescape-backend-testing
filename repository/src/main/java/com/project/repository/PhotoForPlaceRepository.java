package com.project.repository;

import com.project.domain.PhotoForPlace;
import com.project.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoForPlaceRepository extends JpaRepository<PhotoForPlace, Long> {
    List<PhotoForPlace> findAllByPlace(Place place);
}
