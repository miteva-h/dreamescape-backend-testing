package com.project.domain.dto;

import com.project.domain.identity.User;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDetailsDto {
    private String username;
    private SimpleGrantedAuthority role;

    public static UserDetailsDto of(User user) {
        UserDetailsDto details = new UserDetailsDto();
        details.username = user.getUsername();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));
        details.role = authorities.get(0);
        return details;
    }
}
