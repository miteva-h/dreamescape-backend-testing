package com.project.service.implementation;

import com.project.domain.Accommodation;
import com.project.domain.Review;
import com.project.domain.dto.ReviewDto;
import com.project.domain.exceptions.AccommodationNotFoundException;
import com.project.domain.exceptions.UserNotFoundException;
import com.project.domain.identity.User;
import com.project.repository.AccommodationRepository;
import com.project.repository.ReviewRepository;
import com.project.repository.UserRepository;
import com.project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    public List<Review> findByAccommodation(Long accommodationId) {
        return this.reviewRepository.findAllByAccommodation_Id(accommodationId);
    }

    @Override
    public Optional<Review> add(ReviewDto reviewDto) {
        User user = this.userRepository.findByUsername(reviewDto.getUsername()).orElseThrow(UserNotFoundException::new);
        Accommodation accommodation = this.accommodationRepository.findById(reviewDto.getAccommodation()).orElseThrow(AccommodationNotFoundException::new);
        Review review = new Review(reviewDto.getReviewText(), reviewDto.getStars(), user, accommodation);
        this.reviewRepository.save(review);
        return Optional.of(review);
    }

    @Override
    public void deleteById(Long id) {
        this.reviewRepository.deleteById(id);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return this.reviewRepository.findById(id);
    }
}
