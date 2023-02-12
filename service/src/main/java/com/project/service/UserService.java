package com.project.service;

import com.project.domain.dto.RegisterDto;
import com.project.domain.identity.Role;
import com.project.domain.identity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> addUser(RegisterDto registerDto);
    Optional<Role> addRole(Role role);
    void addRoleToUser(String username, String roleName);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    List<Role> findAllRoles();
}
