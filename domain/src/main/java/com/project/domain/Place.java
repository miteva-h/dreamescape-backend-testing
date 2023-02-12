package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "place")
    private List<PhotoForPlace> gallery;

    @JsonIgnore
    @OneToMany(mappedBy = "place")
    private List<Accommodation> accommodations;

    public Place(String name,
                 String location,
                 String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }
}
