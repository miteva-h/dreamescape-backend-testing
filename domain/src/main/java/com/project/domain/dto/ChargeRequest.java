package com.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {

    private Integer amount;
    private String currency;
    private String description;
    private String stripeToken;
    private String user;
    //
    private Long arrangement;
}
