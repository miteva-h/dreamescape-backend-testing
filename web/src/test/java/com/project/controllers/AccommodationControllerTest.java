package com.project.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.Accommodation;
import com.project.domain.Place;
import com.project.domain.dto.AccommodationDto;
import com.project.domain.enumerations.TypeOfAccommodation;
import com.project.domain.enumerations.TypeOfBoard;
import com.project.service.AccommodationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccommodationControllerTest {

    @Mock
    private AccommodationService accommodationService;

    @InjectMocks
    private AccommodationController accommodationController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accommodationController).build();
    }

    @Test
    public void findAll_ShouldReturnListOfAccommodation() throws Exception {
        List<Accommodation> accommodations = Arrays.asList(
                new Accommodation(),
                new Accommodation()
        );

        when(accommodationService.findAll()).thenReturn(accommodations);

        mockMvc.perform(get("/accommodations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void findAllByPlace_ShouldReturnListOfAccommodation() throws Exception {
        Place place = new Place();
        Accommodation a1 = new Accommodation();
        a1.setPlace(place);
        Accommodation a2 = new Accommodation();
        a2.setPlace(place);
        List<Accommodation> accommodations = Arrays.asList(a1, a2);

        when(accommodationService.findAllByPlace(1L)).thenReturn(accommodations);

        mockMvc.perform(get("/accommodations/place/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].place").value(place))
                .andExpect(jsonPath("$[1].place").value(place));
    }

    @Test
    public void findById_ShouldReturnResponseEntityOfAccommodation() throws Exception {
        Accommodation accommodation = new Accommodation();

        when(accommodationService.findById(1L)).thenReturn(Optional.of(accommodation));

        mockMvc.perform(get("/accommodations/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findById_ShouldReturnNotFound() throws Exception {
        when(accommodationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/accommodations/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listAllTypesOfAccommodation_ShouldReturnListOfTypesOfAccommodation() throws Exception {
        mockMvc.perform(get("/accommodations/typesOfAccommodation"))
                .andExpect(status().isOk());
    }

    @Test
    public void listAllTypesOfBoard_ShouldReturnListOfTypesOfBoard() throws Exception {
        mockMvc.perform(get("/accommodations/typesOfBoard"))
                .andExpect(status().isOk());
    }

    @Test
    public void add_ShouldReturnResponseEntityOfAccommodation() throws Exception {
        Place place = new Place();
        place.setId(1L);
        AccommodationDto accommodationDto = new AccommodationDto("Test Accommodation",
                TypeOfAccommodation.CONTEMPORARY_CABIN, TypeOfBoard.ALL_INCLUSIVE,
                "Test Description", 1L, 10.0, null);

        Accommodation accommodation = new Accommodation("Test Accommodation",
                TypeOfAccommodation.CONTEMPORARY_CABIN, TypeOfBoard.ALL_INCLUSIVE,
                "Test Description", place, 10.0, null);

        when(accommodationService.add(accommodationDto)).thenReturn(Optional.of(accommodation));

        mockMvc.perform(post("/accommodations/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accommodationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(accommodationDto.getName())))
                .andExpect(jsonPath("$.description", is(accommodationDto.getDescription())))
                .andExpect(jsonPath("$.pricePerNight", is(accommodationDto.getPricePerNight())));

        verify(accommodationService, times(1)).add(accommodationDto);
    }

    @Test
    public void edit_ShouldReturnResponseEntityOfAccommodation() throws Exception {
        AccommodationDto accommodationDto = new AccommodationDto("Test Accommodation",
                TypeOfAccommodation.CONTEMPORARY_CABIN, TypeOfBoard.ALL_INCLUSIVE,
                "Test Description", 1L, 10.0, null);        Accommodation savedAccommodation = new Accommodation();
        savedAccommodation.setId(1L);
        when(accommodationService.edit(1L, accommodationDto)).thenReturn(Optional.of(savedAccommodation));

        mockMvc.perform(put("/accommodations/1/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accommodationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteById_ShouldReturnResponseEntity() throws Exception {
        doNothing().when(accommodationService).deleteById(1L);

        mockMvc.perform(delete("/accommodations/1/delete"))
                .andExpect(status().isOk());

        verify(accommodationService, times(1)).deleteById(1L);
    }


}