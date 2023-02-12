package com.project.domain.dto;

import lombok.Data;

@Data
public class ReviewDto {

    private String reviewText;

    private Integer stars;

    private String username;

    private Long accommodation;

    public ReviewDto() {
    }

    public ReviewDto(String reviewText, Integer stars, String username, Long accommodation) {
        this.reviewText = reviewText;
        this.stars = stars;
        this.username = username;
        this.accommodation = accommodation;
    }
}
