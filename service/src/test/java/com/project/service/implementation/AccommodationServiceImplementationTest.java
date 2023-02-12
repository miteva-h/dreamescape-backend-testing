package com.project.service.implementation;

import com.project.domain.Accommodation;
import com.project.domain.Place;
import com.project.domain.dto.AccommodationDto;
import com.project.domain.enumerations.TypeOfAccommodation;
import com.project.domain.enumerations.TypeOfBoard;
import com.project.repository.AccommodationRepository;
import com.project.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccommodationServiceImplementationTest {

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private AccommodationServiceImplementation accommodationService;

    @Test
    public void findAll_ShouldReturnListOfAccommodations() {
        Accommodation a1 = new Accommodation();
        Accommodation a2 = new Accommodation();
        Accommodation a3 = new Accommodation();
        Accommodation a4 = new Accommodation();
        Accommodation a5 = new Accommodation();

        when(this.accommodationRepository.findAll()).thenReturn(Arrays.asList(a1, a2, a3, a4, a5));

        List<Accommodation> result = accommodationService.findAll();

        verify(accommodationRepository, times(1)).findAll();
        assertThat(result).containsExactlyInAnyOrder(a1, a2, a3, a4, a5);
    }

    @Test
    public void findAllByPlace_ShouldReturnListOfAccommodations() {
        Place place = new Place();
        place.setId(1L);
        Accommodation a1 = new Accommodation();
        a1.setPlace(place);
        Accommodation a2 = new Accommodation();
        a2.setPlace(place);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(accommodationRepository.findAllByPlace(place)).thenReturn(Arrays.asList(a1, a2));

        List<Accommodation> result = accommodationService.findAllByPlace(1L);

        verify(placeRepository, times(1)).findById(1L);
        verify(accommodationRepository, times(1)).findAllByPlace(place);
        assertThat(result).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    public void findById_ShouldReturnOptionalOfAccommodation() {
        Accommodation a1 = new Accommodation();
        a1.setId(1L);

        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(a1));

        Optional<Accommodation> result = accommodationService.findById(1L);

        verify(accommodationRepository, times(1)).findById(1L);
        assertThat(result).isEqualTo(Optional.of(a1));
    }

    @Test
    public void checkIfPresent_ShouldReturnSuccess() {
        AccommodationDto accommodationDto = new AccommodationDto("Test Accommodation", null, null, "", null, 0.0, null);
        Accommodation accommodation = new Accommodation();
        accommodation.setName("Test Accommodation");
        when(accommodationRepository.findAll()).thenReturn(Collections.singletonList(accommodation));

        Boolean result = accommodationService.checkIfPresent(accommodationDto);

        assertTrue(result);
    }

    @Test
    public void checkIfPresent_ShouldReturnFailure() {
        AccommodationDto accommodationDto = new AccommodationDto("Test Accommodation", null, null, "", null, 0.0, null);
        when(accommodationRepository.findAll()).thenReturn(Collections.emptyList());

        Boolean result = accommodationService.checkIfPresent(accommodationDto);

        assertFalse(result);
    }

    @Test
    public void checkIfPresent_NegativeTest() {
        AccommodationDto accommodationDto = new AccommodationDto("Test Accommodation", null, null, "", null, 0.0, null);
        Accommodation accommodation = new Accommodation();
        accommodation.setName("Different Name");
        when(accommodationRepository.findAll()).thenReturn(Collections.singletonList(accommodation));

        Boolean result = accommodationService.checkIfPresent(accommodationDto);

        assertFalse(result);
    }

    @Test
    public void add_ShouldReturnOptionalOfAccommodation() {
        AccommodationDto ad1 = new AccommodationDto("Test Accommodation", TypeOfAccommodation.PRIVATE_VILLA, TypeOfBoard.FULL_BOARD, "Test Description", 1L, 100.0, "Test Photo");
        Place place = new Place();
        place.setId(1L);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(accommodationRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<Accommodation> result = accommodationService.add(ad1);

        verify(placeRepository).findById(1L);
        verify(accommodationRepository).findAll();
        verify(accommodationRepository).save(any(Accommodation.class));
        assertTrue(result.isPresent());
        Accommodation accommodation = result.get();
        assertEquals("Test Accommodation", accommodation.getName());
        assertEquals(TypeOfAccommodation.PRIVATE_VILLA, accommodation.getTypeOfAccommodation());
        assertEquals(TypeOfBoard.FULL_BOARD, accommodation.getTypeOfBoard());
        assertEquals("Test Description", accommodation.getDescription());
        assertEquals(place, accommodation.getPlace());
        assertEquals(100.0, accommodation.getPricePerNight(), 0.001);
        assertEquals("Test Photo", accommodation.getPhoto());
    }

    @Test
    public void add_TestIfAlreadyExists() {
        AccommodationDto ad1 = new AccommodationDto("Test Accommodation", TypeOfAccommodation.PRIVATE_VILLA, TypeOfBoard.FULL_BOARD, "Test Description", 1L, 100.0, "Test Photo");

        when(accommodationRepository.findAll()).thenReturn(Collections.singletonList(new Accommodation("Test Accommodation", TypeOfAccommodation.CONTEMPORARY_CABIN, TypeOfBoard.HALF_BOARD, "", null, 0.0, "")));

        Optional<Accommodation> result = accommodationService.add(ad1);

        verify(accommodationRepository).findAll();
        verify(accommodationRepository, never()).save(any(Accommodation.class));
        assertFalse(result.isPresent());
    }

    @Test
    public void edit_ShouldReturnOptionalOfAccommodation() {
        AccommodationDto ad1 = new AccommodationDto("Test Accommodation", TypeOfAccommodation.PRIVATE_VILLA, TypeOfBoard.FULL_BOARD, "Test Description", 2L, 100.0, "Test Photo");
        Place place = new Place();
        place.setId(2L);
        place.setName("Test Place");
        Accommodation a1 = new Accommodation("Old Accommodation", TypeOfAccommodation.PRIVATE_ESTATE, TypeOfBoard.HALF_BOARD, "Old Description", place, 50.0, "Old Photo");
        a1.setId(1L);

        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(a1));
        when(placeRepository.findById(ad1.getPlace())).thenReturn(Optional.of(place));

        Optional<Accommodation> result = accommodationService.edit(1L, ad1);

        assertTrue(result.isPresent());
        assertEquals("Test Accommodation", result.get().getName());
        assertEquals(TypeOfAccommodation.PRIVATE_VILLA, result.get().getTypeOfAccommodation());
        assertEquals(TypeOfBoard.FULL_BOARD, result.get().getTypeOfBoard());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(2L, result.get().getPlace().getId().longValue());
        assertEquals("Test Place", result.get().getPlace().getName());
        assertEquals(100.0, result.get().getPricePerNight(), 0.0);
        assertEquals("Test Photo", result.get().getPhoto());

        verify(accommodationRepository).save(result.get());
        assertSame(place, result.get().getPlace());
    }

    @Test
    public void edit_ShouldSetPlace() {
        AccommodationDto ad1 = new AccommodationDto("Test Accommodation", TypeOfAccommodation.PRIVATE_VILLA, TypeOfBoard.FULL_BOARD, "Test Description", 2L, 100.0, "Test Photo");
        Place place = new Place();
        place.setId(2L);
        place.setName("Test Place");
        Accommodation a1 = new Accommodation("Old Accommodation", TypeOfAccommodation.PRIVATE_ESTATE, TypeOfBoard.HALF_BOARD, "Old Description", null, 50.0, "Old Photo");
        a1.setId(1L);

        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(a1));
        when(placeRepository.findById(ad1.getPlace())).thenReturn(Optional.of(place));

        Optional<Accommodation> result = accommodationService.edit(1L, ad1);

        assertTrue(result.isPresent());
        assertEquals("Test Accommodation", result.get().getName());
        assertEquals(TypeOfAccommodation.PRIVATE_VILLA, result.get().getTypeOfAccommodation());
        assertEquals(TypeOfBoard.FULL_BOARD, result.get().getTypeOfBoard());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(2L, result.get().getPlace().getId().longValue());
        assertEquals("Test Place", result.get().getPlace().getName());
        assertEquals(100.0, result.get().getPricePerNight(), 0.0);
        assertEquals("Test Photo", result.get().getPhoto());

        assertEquals(place, result.get().getPlace());

        verify(accommodationRepository).save(result.get());
    }

    @Test
    public void delete_ShouldReturnVoid() {
        Long id = 1L;

        accommodationService.deleteById(id);
        verify(accommodationRepository).deleteById(id);
    }
}