package com.project.service.implementation;

import com.project.domain.Accommodation;
import com.project.domain.Review;
import com.project.domain.dto.ReviewDto;
import com.project.domain.identity.User;
import com.project.repository.AccommodationRepository;
import com.project.repository.ReviewRepository;
import com.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplementationTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewService;

    @Test
    public void findByAccommodation_ShouldReturnListOfReviews() {
        when(reviewRepository.findAllByAccommodation_Id(1L)).thenReturn(Arrays.asList(new Review(), new Review()));

        List<Review> result = reviewService.findByAccommodation(1L);

        assertEquals(2, result.size());
        verify(reviewRepository).findAllByAccommodation_Id(1L);
    }

    @Test
    public void add_ShouldReturnOptionalOfReview() {
        ReviewDto reviewDto = new ReviewDto("Test Review", 5, "Test User", 1L);
        User user = new User();
        user.setUsername("Test User");
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        when(userRepository.findByUsername("Test User")).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));

        Optional<Review> result = reviewService.add(reviewDto);

        assertTrue(result.isPresent());
        assertEquals("Test Review", result.get().getReviewText());
        assertEquals(5, result.get().getStars());
        assertEquals("Test User", result.get().getUser().getUsername());
        assertEquals(1L, result.get().getAccommodation().getId().longValue());

        verify(reviewRepository).save(result.get());
    }

    @Test
    public void deleteById_ShouldCallRepositoryDeleteById() {
        reviewService.deleteById(1L);

        verify(reviewRepository).deleteById(1L);
    }

    @Test
    public void findById_ShouldReturnOptionalOfReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(new Review()));

        Optional<Review> result = reviewService.findById(1L);

        assertTrue(result.isPresent());

        verify(reviewRepository).findById(1L);
    }


}