package com.project.domain.dto;

import com.project.domain.enumerations.TypeOfAccommodation;
import com.project.domain.enumerations.TypeOfBoard;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccommodationDto {

    private String name;

    private TypeOfAccommodation typeOfAccommodation;

    private TypeOfBoard typeOfBoard;

    private String description;

    private Long place;

    private Double pricePerNight;

    private String photo;

    public AccommodationDto(String name,
                            TypeOfAccommodation typeOfAccommodation,
                            TypeOfBoard typeOfBoard,
                            String description,
                            Long place,
                            Double pricePerNight,
                            String photo) {
        this.name = name;
        this.typeOfAccommodation = typeOfAccommodation;
        this.typeOfBoard = typeOfBoard;
        this.description = description;
        this.place = place;
        this.pricePerNight = pricePerNight;
        this.photo = photo;
    }
}
