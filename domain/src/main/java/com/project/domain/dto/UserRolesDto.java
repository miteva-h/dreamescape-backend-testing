package com.project.domain.dto;

import lombok.Data;

@Data
public class UserRolesDto {

    private String username;
    private String roleName;

    public UserRolesDto(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }
}
