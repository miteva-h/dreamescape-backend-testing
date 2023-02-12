package com.project.domain.dto;

import lombok.Data;

@Data
public class ArrangementDto {

    private String from_date;

    private String to_date;

    private Long accommodation;

    // to make the arrangement and add to user's shopping cart
    private Double price;

    private String username;

    public ArrangementDto() {
    }

    public ArrangementDto(String from_date, String to_date, Long accommodation, Double price, String username) {
        this.from_date = from_date;
        this.to_date = to_date;
        this.accommodation = accommodation;
        this.price = price;
        this.username = username;
    }
}
