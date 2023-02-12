package com.project.controllers;

import com.project.domain.Review;
import com.project.domain.dto.ReviewDto;
import com.project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/accommodation/{id}")
    public List<Review> findAllByAccommodation(@PathVariable Long id) {
        return this.reviewService.findByAccommodation(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Review> add(@RequestBody ReviewDto reviewDto) {
        return this.reviewService.add(reviewDto)
                .map(review -> ResponseEntity.ok().body(review))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.reviewService.deleteById(id);
        if (this.reviewService.findById(id).isEmpty())
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
