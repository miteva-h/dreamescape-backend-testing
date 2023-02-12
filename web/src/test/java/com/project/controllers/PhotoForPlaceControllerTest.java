package com.project.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.PhotoForPlace;
import com.project.domain.Place;
import com.project.domain.dto.PhotoForPlaceDto;
import com.project.service.PhotoForPlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PhotoForPlaceControllerTest {

    @Mock
    private PhotoForPlaceService photoForPlaceService;

    @InjectMocks
    private PhotoForPlaceController photoForPlaceController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(photoForPlaceController).build();
    }

    @Test
    public void findAll_ShouldReturnListOfPhotosForPlace() throws Exception {
        Place place = new Place();
        Long placeId = 1L;
        place.setId(placeId);
        List<PhotoForPlace> photosForPlace = new ArrayList<>();
        photosForPlace.add(new PhotoForPlace("image1", place));
        photosForPlace.add(new PhotoForPlace("image2", place));

        when(photoForPlaceService.findAll(placeId)).thenReturn(photosForPlace);

        mockMvc.perform(get("/photos/place/{id}", placeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].place.id", is(1)))
                .andExpect(jsonPath("$[0].photoURL", is("image1")))
                .andExpect(jsonPath("$[1].place.id", is(1)))
                .andExpect(jsonPath("$[1].photoURL", is("image2")));

        verify(photoForPlaceService, times(1)).findAll(placeId);


    }

    @Test
    public void findById_ShouldReturnResponseEntityOfPhotoForPlace() throws Exception {
        Place place = new Place();
        Long placeId = 1L;
        place.setId(placeId);
        PhotoForPlace photoForPlace = new PhotoForPlace("image1", place);
        Long id = 2L;
        photoForPlace.setId(id);

        when(photoForPlaceService.findById(id)).thenReturn(Optional.of(photoForPlace));

        mockMvc.perform(get("/photos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.place.id", is(1)))
                .andExpect(jsonPath("$.photoURL", is("image1")));

        verify(photoForPlaceService, times(1)).findById(id);
    }

    @Test
    public void findById_ShouldReturnBadRequest() throws Exception {
        Long id = 1L;

        when(photoForPlaceService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/photos/{id}", id))
                .andExpect(status().isBadRequest());

        verify(photoForPlaceService, times(1)).findById(id);
    }

    @Test
    public void add_ShouldReturnResponseEntityOfPhotoForPlace() throws Exception {
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("image1");

        Place place  = new Place();
        Long id = 1L;
        place.setId(id);
        PhotoForPlace photoForPlace = new PhotoForPlace("image1", place);

        when(photoForPlaceService.add(id, photoForPlaceDto)).thenReturn(Optional.of(photoForPlace));

        mockMvc.perform(post("/photos/add/place/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(photoForPlaceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoURL", is(photoForPlaceDto.getPhotoURL())));

        verify(photoForPlaceService, times(1)).add(id, photoForPlaceDto);
    }

    @Test
    public void add_ShouldReturnBadRequest() {
        Long placeId = 1L;
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto();
        photoForPlaceDto.setPhotoURL("test.jpg");

        when(photoForPlaceService.add(placeId, photoForPlaceDto)).thenReturn(Optional.empty());

        ResponseEntity<PhotoForPlace> response = photoForPlaceController.add(placeId, photoForPlaceDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void edit_ShouldReturnResponseEntityOfPhotoForPlace() {
        Long id = 1L;
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto();
        photoForPlaceDto.setPhotoURL("test.jpg");
        PhotoForPlace photoForPlace = new PhotoForPlace();
        photoForPlace.setId(id);
        photoForPlace.setPhotoURL(photoForPlaceDto.getPhotoURL());

        when(photoForPlaceService.edit(id, photoForPlaceDto)).thenReturn(Optional.of(photoForPlace));

        ResponseEntity<PhotoForPlace> response = photoForPlaceController.edit(id, photoForPlaceDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photoForPlace, response.getBody());
    }

    @Test
    public void edit_ShouldReturnBadRequest() {
        Long id = 1L;
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto();
        photoForPlaceDto.setPhotoURL("test.jpg");

        when(photoForPlaceService.edit(id, photoForPlaceDto)).thenReturn(Optional.empty());

        ResponseEntity<PhotoForPlace> response = photoForPlaceController.edit(id, photoForPlaceDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deleteById_ShouldReturnResponseEntity() throws Exception {
        doNothing().when(photoForPlaceService).deleteById(1L);

        mockMvc.perform(delete("/photos/1/delete"))
                .andExpect(status().isOk());

        verify(photoForPlaceService, times(1)).deleteById(1L);
    }



}