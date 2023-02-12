package com.project.domain;

import com.project.domain.identity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewText;

    private Integer stars;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    public Review(String reviewText,
                  Integer stars,
                  User user,
                  Accommodation accommodation) {
        this.reviewText = reviewText;
        this.stars = stars;
        this.user = user;
        this.accommodation = accommodation;
    }
}
