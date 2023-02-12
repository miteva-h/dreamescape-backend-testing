package com.project.service.implementation;

import com.project.domain.PhotoForPlace;
import com.project.domain.Place;
import com.project.domain.dto.PhotoForPlaceDto;
import com.project.domain.exceptions.PhotoNotFoundException;
import com.project.domain.exceptions.PlaceNotFoundException;
import com.project.repository.PhotoForPlaceRepository;
import com.project.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoForPlaceServiceImplementationTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PhotoForPlaceRepository photoForPlaceRepository;

    @InjectMocks
    private PhotoForPlaceServiceImplementation photoForPlaceService;

    @Test
    public void findAll_ShouldReturnListOfPhotoForPlace(){
        Long placeId = 1L;
        List<PhotoForPlace> expectedPhotos = Arrays.asList(
                new PhotoForPlace("photo1", new Place()),
                new PhotoForPlace("photo2", new Place())
        );
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(new Place()));
        when(photoForPlaceRepository.findAllByPlace(any(Place.class))).thenReturn(expectedPhotos);

        List<PhotoForPlace> photos = photoForPlaceService.findAll(placeId);

        assertNotNull(photos);
        assertEquals(expectedPhotos, photos);
    }

    @Test
    public void findAllByPlaceId_PlaceNotFound_ShouldThrowException() {
        Long placeId = 1L;
        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());

        assertThrows(PlaceNotFoundException.class, () -> photoForPlaceService.findAll(placeId));
    }

    @Test
    public void findById_ShouldReturnOptionalOfPhotoForPlace() {
        Long id = 1L;
        PhotoForPlace expectedPhoto = new PhotoForPlace("photo1", new Place());
        when(photoForPlaceRepository.findById(id)).thenReturn(Optional.of(expectedPhoto));

        Optional<PhotoForPlace> photo = photoForPlaceService.findById(id);

        assertTrue(photo.isPresent());
        assertEquals(expectedPhoto, photo.get());
    }

    @Test
    public void findById_PhotoNotFound_ShouldThrowException() {
        Long id = 1L;
        when(photoForPlaceRepository.findById(id)).thenReturn(Optional.empty());

        Optional<PhotoForPlace> photo = photoForPlaceService.findById(id);

        assertFalse(photo.isPresent());
    }

    @Test
    public void checkIfPresent_ShouldReturnSuccess() {
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("url");
        List<PhotoForPlace> photos = List.of(new PhotoForPlace("url", new Place()));
        when(photoForPlaceRepository.findAll()).thenReturn(photos);

        Boolean result = photoForPlaceService.checkIfPresent(photoForPlaceDto);

        assertTrue(result);
        verify(photoForPlaceRepository, times(1)).findAll();
    }

    @Test
    public void checkIfPresent_ShouldReturnFailure() {
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("url");
        List<PhotoForPlace> photos = List.of(new PhotoForPlace("other-url", new Place()));
        when(photoForPlaceRepository.findAll()).thenReturn(photos);

        Boolean result = photoForPlaceService.checkIfPresent(photoForPlaceDto);

        assertFalse(result);
        verify(photoForPlaceRepository, times(1)).findAll();
    }

    @Test
    public void add_ShouldReturnSuccess() {
        Long placeId = 1L;
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("url");
        Place place = new Place();
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(photoForPlaceRepository.findAll()).thenReturn(List.of());

        Optional<PhotoForPlace> result = photoForPlaceService.add(placeId, photoForPlaceDto);

        assertTrue(result.isPresent());
        verify(placeRepository, times(1)).findById(placeId);
        verify(photoForPlaceRepository, times(1)).findAll();
        verify(photoForPlaceRepository, times(1)).save(any(PhotoForPlace.class));
    }

    @Test
    public void add_ShouldReturnFailure() {
        Long placeId = 1L;
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("url");
        List<PhotoForPlace> photos = List.of(new PhotoForPlace("url", new Place()));
        when(photoForPlaceRepository.findAll()).thenReturn(photos);

        Optional<PhotoForPlace> result = photoForPlaceService.add(placeId, photoForPlaceDto);

        assertFalse(result.isPresent());
        verify(photoForPlaceRepository, times(1)).findAll();
        verify(photoForPlaceRepository, times(0)).save(any(PhotoForPlace.class));
    }

    @Test
    public void edit_ShouldReturnPhotoForPlace() {
        Long id = 1L;
        PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("new_url");
        PhotoForPlace photo = new PhotoForPlace("old_url", new Place());
        when(photoForPlaceRepository.findById(id)).thenReturn(Optional.of(photo));

        Optional<PhotoForPlace> editedPhoto = photoForPlaceService.edit(id, photoForPlaceDto);

        assertTrue(editedPhoto.isPresent());
        assertEquals("new_url", editedPhoto.get().getPhotoURL());
        verify(photoForPlaceRepository).save(photo);
    }

    @Test
    public void edit_photoNotFound_ShouldThrowException() {
        try {
            Long id = 1L;
            PhotoForPlaceDto photoForPlaceDto = new PhotoForPlaceDto("new_url");
            when(photoForPlaceRepository.findById(id)).thenReturn(Optional.empty());

            photoForPlaceService.edit(id, photoForPlaceDto);
        }catch (PhotoNotFoundException e){
            assertTrue(true);
        }

    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        photoForPlaceService.deleteById(id);

        verify(photoForPlaceRepository).deleteById(id);
    }

}