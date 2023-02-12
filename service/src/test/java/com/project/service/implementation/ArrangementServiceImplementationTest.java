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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArrangementServiceImplementationTest {

    @Mock
    private ArrangementRepository arrangementRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArrangementInShoppingCartRepository arrangementInShoppingCartRepository;

    @InjectMocks
    private ArrangementServiceImplementation arrangementService;

    @Test
    public void findAllByAccommodation_ShouldReturnListOfArrangements() {
        Long accommodationId = 1L;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);

        Arrangement a1 = new Arrangement();
        Arrangement a2 = new Arrangement();
        List<Arrangement> arrangements = Arrays.asList(a1, a2);

        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(arrangementRepository.findAllByAccommodation(accommodation)).thenReturn(arrangements);

        List<Arrangement> result = arrangementService.findAllByAccommodation(accommodationId);
        assertThat(result).isEqualTo(arrangements);
    }

    @Test
    public void getAllDatesForAccommodation_ShouldReturnListOfDates() {
        Long accommodationId = 1L;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);

        LocalDate from1 = LocalDate.of(2022, 01, 01);
        LocalDate to1 = LocalDate.of(2022, 01, 02);
        LocalDate from2 = LocalDate.of(2022, 01, 02);
        LocalDate to2 = LocalDate.of(2022, 01, 03);
        Arrangement a1 = new Arrangement(from1, to1, accommodation);
        Arrangement a2 = new Arrangement(from2, to2, accommodation);
        List<Arrangement> arrangements = Arrays.asList(a1, a2);
        List<LocalDate> dates = Arrays.asList(to1);

        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(arrangementService.findAllByAccommodation(accommodationId)).thenReturn(arrangements);

        List<LocalDate> result = arrangementService.getAllDatesForAccommodation(accommodationId);
        assertThat(result).isEqualTo(dates);
    }

    @Test
    public void add_ShouldReturnArrangement() {
        ArrangementDto arrangementDto = new ArrangementDto();
        arrangementDto.setAccommodation(1L);
        arrangementDto.setFrom_date("2022-01-01");
        arrangementDto.setTo_date("2022-01-10");
        arrangementDto.setUsername("user1");
        arrangementDto.setPrice(100.0);

        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));

        User user = new User();
        user.setUsername("user1");
        ShoppingCart shoppingCart = new ShoppingCart();
        user.setShoppingCart(shoppingCart);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        Arrangement arrangement = new Arrangement();
        arrangement.setAccommodation(accommodation);
        arrangement.setFrom_date(LocalDate.of(2022,01,01));
        arrangement.setTo_date(LocalDate.of(2022,01,10));
        when(arrangementRepository.save(any(Arrangement.class))).thenReturn(arrangement);

        ArrangementInShoppingCart arrangementInShoppingCart = new ArrangementInShoppingCart();
        when(arrangementInShoppingCartRepository.save(any(ArrangementInShoppingCart.class))).thenReturn(arrangementInShoppingCart);

        Optional<Arrangement> result = arrangementService.add(arrangementDto);
        assertTrue(result.isPresent());
        assertEquals(arrangement, result.get());
        verify(accommodationRepository).findById(1L);
        verify(userRepository).findByUsername("user1");
        verify(arrangementRepository).save(any(Arrangement.class));
        verify(arrangementInShoppingCartRepository).save(any(ArrangementInShoppingCart.class));
    }

    @Test
    public void add_AccommodationNotFound_ShouldThrowException() {
        try {
            ArrangementDto arrangementDto = new ArrangementDto();
            arrangementDto.setAccommodation(1L);
            arrangementDto.setFrom_date("2022-01-01");
            arrangementDto.setTo_date("2022-01-10");
            arrangementDto.setUsername("user1");
            arrangementDto.setPrice(100.0);

            when(accommodationRepository.findById(1L)).thenReturn(Optional.empty());

            arrangementService.add(arrangementDto);
            verify(accommodationRepository).findById(1L);
        } catch (AccommodationNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void add_UserNotFound_ShouldThrowException() {
        try {
            ArrangementDto arrangementDto = new ArrangementDto();
            arrangementDto.setAccommodation(1L);
            arrangementDto.setFrom_date("2022-01-01");
            arrangementDto.setTo_date("2022-01-10");
            arrangementDto.setUsername("user1");
            arrangementDto.setPrice(100.0);

            Accommodation accommodation = new Accommodation();
            accommodation.setId(1L);
            when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));

            when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

            arrangementService.add(arrangementDto);
            verify(userRepository).findByUsername("user1");
        } catch (UserNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void getAllArrangementsForUser_ShouldReturnListOfArrangements() {
        User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
        user.setShoppingCart(shoppingCart);
        when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));

        List<ArrangementInShoppingCart> expectedArrangements = Arrays.asList(
                new ArrangementInShoppingCart(),
                new ArrangementInShoppingCart()
        );
        when(arrangementInShoppingCartRepository.findAllByShoppingCart(shoppingCart)).thenReturn(expectedArrangements);

        List<ArrangementInShoppingCart> actualArrangements = arrangementService.getAllArrangementsForUser("testUsername");
        assertEquals(expectedArrangements, actualArrangements);
    }

    @Test
    public void findById_ShouldReturnArrangement() {
        Long id = 1L;
        Arrangement arrangement = new Arrangement();

        when(arrangementRepository.findById(id)).thenReturn(Optional.of(arrangement));

        Optional<Arrangement> result = arrangementService.findById(id);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(arrangement, result.get());
    }

    @Test
    public void findById_ArrangementNotFound_ShouldThrowException() {
        try {
            Long id = 1L;
            when(arrangementRepository.findById(id)).thenReturn(Optional.empty());
            Optional<Arrangement> result = arrangementService.findById(id);
            verify(arrangementRepository).findById(id);
        } catch (ArrangementNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteById_ShouldReturnVoid() {
        Long id = 1L;
        ArrangementInShoppingCart arrangementInShoppingCart = new ArrangementInShoppingCart();
        Arrangement arrangement = new Arrangement();

        when(arrangementInShoppingCartRepository.findById(id)).thenReturn(Optional.of(arrangementInShoppingCart));
        when(arrangementRepository.findByArrangementInShoppingCarts(arrangementInShoppingCart)).thenReturn(arrangement);

        arrangementService.deleteById(id);

        verify(arrangementInShoppingCartRepository, times(1)).deleteById(id);
        verify(arrangementRepository, times(1)).deleteById(arrangement.getId());
    }


}