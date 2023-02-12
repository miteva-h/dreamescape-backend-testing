package com.project.service.implementation;

import com.project.domain.Accommodation;
import com.project.domain.Arrangement;
import com.project.domain.ShoppingCart;
import com.project.domain.dto.ArrangementDto;
import com.project.domain.exceptions.AccommodationNotFoundException;
import com.project.domain.exceptions.ArrangementNotFoundException;
import com.project.domain.exceptions.UserNotFoundException;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInShoppingCart;
import com.project.repository.AccommodationRepository;
import com.project.repository.ArrangementInShoppingCartRepository;
import com.project.repository.ArrangementRepository;
import com.project.repository.UserRepository;
import com.project.service.ArrangementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArrangementServiceImplementation implements ArrangementService {

    private final ArrangementRepository arrangementRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final ArrangementInShoppingCartRepository arrangementInShoppingCartRepository;

    @Override
    public List<Arrangement> findAllByAccommodation(Long accommodationId) {
        Accommodation accommodation = this.accommodationRepository.findById(accommodationId).orElseThrow(AccommodationNotFoundException::new);
        return arrangementRepository.findAllByAccommodation(accommodation);
    }

    @Override
    public List<LocalDate> getAllDatesForAccommodation(Long accommodationId) {
        List<Arrangement> arrangements = findAllByAccommodation(accommodationId);
        List<LocalDate> newList = arrangements.stream()
                .map(arrangement -> arrangement.getDatesBetween())
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
        arrangements.stream()
                .filter(x -> arrangements.stream().anyMatch(y -> x.getTo_date().isEqual(y.getFrom_date())))
                .forEach(x -> newList.add(x.getTo_date()));
        return newList;
    }

    @Override
    public Optional<Arrangement> add(ArrangementDto arrangementDto) {
        Accommodation accommodation = this.accommodationRepository.findById(arrangementDto.getAccommodation()).orElseThrow(AccommodationNotFoundException::new);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateFrom = LocalDate.parse(arrangementDto.getFrom_date(), formatter);
        LocalDate localDateTo = LocalDate.parse(arrangementDto.getTo_date(), formatter);

        Arrangement arrangement = new Arrangement(localDateFrom, localDateTo, accommodation);
        this.arrangementRepository.save(arrangement);
        User user = this.userRepository.findByUsername(arrangementDto.getUsername()).orElseThrow(UserNotFoundException::new);
        ShoppingCart shoppingCart = user.getShoppingCart();
        ArrangementInShoppingCart arrangementInShoppingCart = new ArrangementInShoppingCart(arrangement,
                shoppingCart, localDateFrom, localDateTo, arrangementDto.getPrice());
        this.arrangementInShoppingCartRepository.save(arrangementInShoppingCart);
        return Optional.of(arrangement);
    }

    @Override
    public List<ArrangementInShoppingCart> getAllArrangementsForUser(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        ShoppingCart shoppingCart = user.getShoppingCart();
        return arrangementInShoppingCartRepository.findAllByShoppingCart(shoppingCart);
    }

    @Override
    public Optional<Arrangement> findById(Long id) {
        return this.arrangementRepository.findById(id);
    }


    @Override
    public void deleteById(Long id) {
        ArrangementInShoppingCart arrangementInShoppingCart = this.arrangementInShoppingCartRepository.findById(id).orElseThrow(ArrangementNotFoundException::new);
        Arrangement arrangement = this.arrangementRepository.findByArrangementInShoppingCarts(arrangementInShoppingCart);
        this.arrangementInShoppingCartRepository.deleteById(id);
        this.arrangementRepository.deleteById(arrangement.getId());
    }
}