package com.project.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceDto {

    private String name;

    private String location;

    private String description;

    public PlaceDto(String name,
                 String location,
                 String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }
}
