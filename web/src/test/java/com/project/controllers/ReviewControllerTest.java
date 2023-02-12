package com.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.Review;
import com.project.domain.dto.ReviewDto;
import com.project.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    public void findAllByAccommodation_ShouldReturnListOfReview() throws Exception {
        Long id = 1L;
        List<Review> reviews = Arrays.asList(
                new Review(),
                new Review()
        );

        when(reviewService.findByAccommodation(id)).thenReturn(reviews);

        mockMvc.perform(get("/reviews/accommodation/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(reviewService, times(1)).findByAccommodation(id);
    }

    @Test
    public void add_ShouldReturnResponseEntityOfReview() throws Exception {
        ReviewDto reviewDto = new ReviewDto();
        Review review = new Review();

        when(reviewService.add(reviewDto)).thenReturn(Optional.of(review));

        mockMvc.perform(post("/reviews/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(review.getId())));

        verify(reviewService, times(1)).add(reviewDto);
    }

    @Test
    public void deleteById_ShouldReturnResponseEntity() throws Exception {
        Long id = 1L;

        doNothing().when(reviewService).deleteById(id);
        when(reviewService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/reviews/{id}/delete", id))
                .andExpect(status().isOk());

        verify(reviewService, times(1)).deleteById(id);
    }


}