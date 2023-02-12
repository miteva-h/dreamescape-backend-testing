package com.project.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterDto {

    private String username;
    private String password;
    private String repeatPassword;
    private String firstName;
    private String lastName;
    private String role;

    public RegisterDto(String username,
                       String password,
                       String repeatPassword,
                       String firstName,
                       String lastName,
                       String role) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
