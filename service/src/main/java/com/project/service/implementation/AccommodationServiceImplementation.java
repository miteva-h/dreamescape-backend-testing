package com.project.service.implementation;

import com.project.domain.Accommodation;
import com.project.domain.Place;
import com.project.domain.dto.AccommodationDto;
import com.project.domain.exceptions.AccommodationNotFoundException;
import com.project.domain.exceptions.PlaceNotFoundException;
import com.project.repository.AccommodationRepository;
import com.project.repository.PlaceRepository;
import com.project.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImplementation implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final PlaceRepository placeRepository;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public List<Accommodation> findAllByPlace(Long placeId) {
        Place place = this.placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
        return accommodationRepository.findAllByPlace(place);
    }

    @Override
    public Optional<Accommodation> findById(Long id) {
        return this.accommodationRepository.findById(id);
    }

    public Boolean checkIfPresent(AccommodationDto accommodationDto) {
        boolean check = this.accommodationRepository.findAll()
                .stream().anyMatch(x -> x.getName().equals(accommodationDto.getName()));
        return check;
    }

    @Override
    public Optional<Accommodation> add(AccommodationDto accommodationDto) {
        if (!checkIfPresent(accommodationDto)) {
            Place place = this.placeRepository.findById(accommodationDto.getPlace()).orElseThrow(PlaceNotFoundException::new);
            Accommodation accommodation = new Accommodation(accommodationDto.getName(), accommodationDto.getTypeOfAccommodation(), accommodationDto.getTypeOfBoard(), accommodationDto.getDescription(), place, accommodationDto.getPricePerNight(), accommodationDto.getPhoto());
            this.accommodationRepository.save(accommodation);
            return Optional.of(accommodation);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Accommodation> edit(Long id, AccommodationDto accommodationDto) {
        Accommodation accommodation = this.accommodationRepository.findById(id).orElseThrow(AccommodationNotFoundException::new);
        accommodation.setName(accommodationDto.getName());
        accommodation.setTypeOfAccommodation(accommodationDto.getTypeOfAccommodation());
        accommodation.setTypeOfBoard(accommodationDto.getTypeOfBoard());
        accommodation.setDescription(accommodationDto.getDescription());
        Place place = this.placeRepository.findById(accommodationDto.getPlace()).orElseThrow(PlaceNotFoundException::new);
        accommodation.setPlace(place);
        accommodation.setPricePerNight(accommodationDto.getPricePerNight());
        accommodation.setPhoto(accommodationDto.getPhoto());
        this.accommodationRepository.save(accommodation);
        return Optional.of(accommodation);
    }

    @Override
    public void deleteById(Long id) {
        this.accommodationRepository.deleteById(id);
    }
}
