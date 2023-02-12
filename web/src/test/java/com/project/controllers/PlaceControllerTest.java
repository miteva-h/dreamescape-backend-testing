package com.project.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.Place;
import com.project.domain.dto.PlaceDto;
import com.project.service.PlaceService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlaceControllerTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private PlaceController placeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(placeController).build();
    }

    @Test
    public void findAll_ShouldReturnListOfPlaces() throws Exception {
        Place p1 = new Place();
        Place p2 = new Place();
        Place p3 = new Place();
        List<Place> expectedPlaces = Arrays.asList(p1, p2, p3);
        when(placeService.findAll()).thenReturn(expectedPlaces);

        mockMvc.perform(get("/places"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void findAllByNameContaining_ShouldReturnListOfPlaces() throws Exception {
        String word = "word";
        List<Place> expectedPlaces = Arrays.asList(new Place(), new Place(), new Place());
        when(placeService.findAllByNameContaining(word)).thenReturn(expectedPlaces);

        mockMvc.perform(get("/places/filter/{word}", word))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void findById_ShouldReturnResponseEntityOfPlace() throws Exception {
        Long id = 1L;
        Place expectedPlace = new Place();
        when(placeService.findById(id)).thenReturn(Optional.of(expectedPlace));

        mockMvc.perform(get("/places/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void findById_ShouldReturnNotFound() throws Exception {
        Long id = 1L;
        when(placeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/places/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void add_ShouldReturnResponseEntityOfPlace() throws Exception {
        PlaceDto placeDto = new PlaceDto("placeName", "placeLocation", "placeDescription");
        Place place = new Place("placeName", "placeLocation", "placeDescription");


        when(placeService.add(placeDto)).thenReturn(Optional.of(place));

        mockMvc.perform(post("/places/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(placeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(placeDto.getName())))
                .andExpect(jsonPath("$.location", is(placeDto.getLocation())))
                .andExpect(jsonPath("$.description", is(placeDto.getDescription())));

        verify(placeService, times(1)).add(placeDto);
    }

    @Test
    public void add_ShouldReturnBadRequest() throws Exception {
        PlaceDto placeDto = new PlaceDto();

        when(placeService.add(placeDto)).thenReturn(Optional.empty());

        mockMvc.perform(post("/places/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(placeDto)))
                .andExpect(status().isBadRequest());

        verify(placeService, times(1)).add(placeDto);
    }

    @Test
    public void edit_ShouldReturnResponseEntityOfPlace() throws Exception {
        PlaceDto placeDto = new PlaceDto();
        Place place = new Place();
        Long id = 1L;

        when(placeService.edit(id, placeDto)).thenReturn(Optional.of(place));

        mockMvc.perform(put("/places/{id}/edit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(placeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(place.getId())));

        verify(placeService, times(1)).edit(id, placeDto);
    }

    @Test
    public void edit_ShouldReturnBadRequest() throws Exception {
        PlaceDto placeDto = new PlaceDto();
        Long id = 1L;

        when(placeService.edit(id, placeDto)).thenReturn(Optional.empty());

        mockMvc.perform(put("/places/{id}/edit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(placeDto)))
                .andExpect(status().isBadRequest());

        verify(placeService, times(1)).edit(id, placeDto);
    }

    @Test
    public void deleteById_ShouldReturnResponseEntity() throws Exception {
        Long id = 1L;

        doNothing().when(placeService).deleteById(id);
        when(placeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/places/{id}/delete", id))
                .andExpect(status().isOk());

        verify(placeService, times(1)).deleteById(id);
    }


}