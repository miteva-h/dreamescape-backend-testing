package com.project.domain.dto;

import lombok.Data;

@Data
public class PhotoForPlaceDto {

    private String photoURL;

    public PhotoForPlaceDto() {

    }

    public PhotoForPlaceDto(String photoURL) {
        this.photoURL = photoURL;
    }
}
