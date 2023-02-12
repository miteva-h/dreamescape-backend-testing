package com.project.service.implementation;

import com.project.domain.PhotoForPlace;
import com.project.domain.Place;
import com.project.domain.dto.PhotoForPlaceDto;
import com.project.domain.exceptions.PhotoNotFoundException;
import com.project.domain.exceptions.PlaceNotFoundException;
import com.project.repository.PhotoForPlaceRepository;
import com.project.repository.PlaceRepository;
import com.project.service.PhotoForPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoForPlaceServiceImplementation implements PhotoForPlaceService {

    private final PlaceRepository placeRepository;
    private final PhotoForPlaceRepository photoForPlaceRepository;

    @Override
    public List<PhotoForPlace> findAll(Long placeId) {
        Place place = this.placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
        return this.photoForPlaceRepository.findAllByPlace(place);
    }

    @Override
    public Optional<PhotoForPlace> findById(Long id) {
        return this.photoForPlaceRepository.findById(id);
    }

    public Boolean checkIfPresent(PhotoForPlaceDto photoForPlaceDto) {
        boolean check = this.photoForPlaceRepository.findAll()
                .stream().anyMatch(x -> x.getPhotoURL().equals(photoForPlaceDto.getPhotoURL()));
        return check;
    }

    @Override
    public Optional<PhotoForPlace> add(Long placeId, PhotoForPlaceDto photoForPlaceDto) {
        if (!checkIfPresent(photoForPlaceDto)) {
            Place place = this.placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
            PhotoForPlace photoForPlace = new PhotoForPlace(photoForPlaceDto.getPhotoURL(), place);
            this.photoForPlaceRepository.save(photoForPlace);
            return Optional.of(photoForPlace);
        }
        return Optional.empty();
    }

    @Override
    public Optional<PhotoForPlace> edit(Long id, PhotoForPlaceDto photoForPlaceDto) {
        PhotoForPlace photo = photoForPlaceRepository.findById(id).orElseThrow(PhotoNotFoundException::new);
        photo.setPhotoURL(photoForPlaceDto.getPhotoURL());
        this.photoForPlaceRepository.save(photo);
        return Optional.of(photo);
    }

    @Override
    public void deleteById(Long id) {
        this.photoForPlaceRepository.deleteById(id);
    }
}
