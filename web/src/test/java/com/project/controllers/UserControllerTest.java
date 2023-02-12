package com.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.config.filters.JWTAuthenticationFilter;
import com.project.domain.dto.RegisterDto;
import com.project.domain.dto.UserRolesDto;
import com.project.domain.identity.Role;
import com.project.domain.identity.User;
import com.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTAuthenticationFilter filter;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void findAll_ShouldReturnListOfUser() throws Exception {
        Role role = new Role();
        List<Role> roles = Collections.singletonList(role);
        User user = new User();
        user.setRoles(roles);
        List<User> users = Collections.singletonList(user);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user.getId())))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())));

        verify(userService, times(1)).findAll();
    }

    @Test
    public void findAllRoles_ShouldReturnListOfRoles() throws Exception {
        Role role = new Role();
        List<Role> roles = Collections.singletonList(role);

        when(userService.findAllRoles()).thenReturn(roles);

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(role.getId())))
                .andExpect(jsonPath("$[0].name", is(role.getName())));

        verify(userService, times(1)).findAllRoles();
    }

    @Test
    public void addUser_ShouldReturnResponseEntityOfUser() throws Exception {
        RegisterDto registerDto = new RegisterDto();

        Role role = new Role();
        List<Role> roles = Collections.singletonList(role);
        User user = new User();
        user.setRoles(roles);

        when(userService.addUser(registerDto)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).addUser(registerDto);
    }

    @Test
    public void doLogin_ShouldReturnJwtToken() throws Exception {
        //String username = "testuser";
        //String password = "password";
        //User user = new User();
        //when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(filter.attemptAuthentication(request, response)).thenReturn(mock(Authentication.class));
        when(filter.generateJwt(response, filter.attemptAuthentication(request, response))).thenReturn("jwt-token");

        String result = userController.doLogin(request, response);

        assertNotNull(result);
        assertEquals("jwt-token", result);
    }

    @Test
    public void addRole_ShouldReturnResponseEntityOfRole() throws Exception {
        Role role = new Role("role-name");

        when(userService.addRole(role)).thenReturn(Optional.of(role));

        mockMvc.perform(post("/api/role/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("role-name"));

        verify(userService, times(1)).addRole(role);
    }

    @Test
    public void addRole_ShouldReturnBadRequest() throws Exception {
        Role role = new Role("role-name");

        when(userService.addRole(role)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/role/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(role)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).addRole(role);
    }

    @Test
    public void addRoleToUser_ShouldResponseEntity() throws Exception {
        UserRolesDto userRolesDto = new UserRolesDto("testuser", "test-role");

        doNothing().when(userService).addRoleToUser(userRolesDto.getUsername(), userRolesDto.getRoleName());

        ResponseEntity result = userController.addRoleToUser(userRolesDto);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }






}