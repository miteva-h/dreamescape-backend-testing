package com.project.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.Accommodation;
import com.project.domain.Arrangement;
import com.project.domain.dto.ArrangementDto;
import com.project.domain.relations.ArrangementInShoppingCart;
import com.project.service.ArrangementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArrangementControllerTest {

    @Mock
    private ArrangementService arrangementService;

    @InjectMocks
    private ArrangementController arrangementController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(arrangementController).build();
    }

    @Test
    public void findAllDatesForAccommodation_ShouldReturnListOfLocalDate() throws Exception {
        Long accommodationId = 1L;
        LocalDate l1 = LocalDate.of(2023, 1, 1);
        LocalDate l2 = LocalDate.of(2023, 1, 2);
        LocalDate l3 = LocalDate.of(2023, 1, 3);
        List<LocalDate> expectedDates = Arrays.asList(l1, l2, l3);

        when(arrangementService.getAllDatesForAccommodation(accommodationId)).thenReturn(expectedDates);

        mockMvc.perform(get("/arrangements/accommodation/{id}", accommodationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(arrangementService, times(1)).getAllDatesForAccommodation(accommodationId);
    }

    @Test
    public void add_ShouldReturnResponseEntityOfArrangement() throws Exception {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        ArrangementDto arrangementDto = new ArrangementDto("2023-01-01", "2023-01-02", 1L, 10.0, "user");
        Arrangement expectedArrangement = new Arrangement(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2), accommodation);

        when(arrangementService.add(arrangementDto)).thenReturn(Optional.of(expectedArrangement));

        mockMvc.perform(post("/arrangements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(arrangementDto)))
                .andExpect(status().isOk());

        verify(arrangementService, times(1)).add(arrangementDto);
    }

    @Test
    public void add_ShouldReturnBadRequest() throws Exception {
        when(arrangementService.add(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/arrangements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllArrangementsForUser_ShouldReturnListOfArrangementsInShoppingCart() throws Exception {
        String username = "test";
        ArrangementInShoppingCart a1 = new ArrangementInShoppingCart();
        a1.setPrice(10.0);
        ArrangementInShoppingCart a2 = new ArrangementInShoppingCart();
        a2.setPrice(10.0);
        List<ArrangementInShoppingCart> arrangements = Arrays.asList(a1, a2);
        when(arrangementService.getAllArrangementsForUser(username)).thenReturn(arrangements);

        mockMvc.perform(get("/arrangements/user/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[1].price").value(10.0));

        verify(arrangementService, times(1)).getAllArrangementsForUser(username);
    }

    @Test
    public void deleteById_ShouldReturnResponseEntity() throws Exception {
        Long id = 1L;
        doNothing().when(arrangementService).deleteById(id);
        when(arrangementService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/arrangements/{id}/delete", id))
                .andExpect(status().isOk());

        verify(arrangementService, times(1)).deleteById(id);
        verify(arrangementService, times(1)).findById(id);
    }

    @Test
    public void deleteById_ShouldReturnNotFound() throws Exception {
        Long id = 1L;
        doNothing().when(arrangementService).deleteById(id);
        when(arrangementService.findById(id)).thenReturn(Optional.of(new Arrangement()));

        mockMvc.perform(delete("/arrangements/{id}/delete", id))
                .andExpect(status().isBadRequest());

        verify(arrangementService, times(1)).deleteById(id);
        verify(arrangementService, times(1)).findById(id);
    }

}