package com.project.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class PhotoForPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoURL;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    public PhotoForPlace(String photoURL,
                         Place place) {
        this.photoURL = photoURL;
        this.place = place;
    }
}
