package com.project.service;

import com.project.domain.Review;
import com.project.domain.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<Review> findByAccommodation(Long accommodationId);

    Optional<Review> add(ReviewDto reviewDto);

    void deleteById(Long id);

    Optional<Review> findById(Long id);

}
