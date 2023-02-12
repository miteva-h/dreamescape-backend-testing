package com.project.service.implementation;

import com.project.domain.Arrangement;
import com.project.domain.Order;
import com.project.domain.dto.ChargeRequest;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInOrder;
import com.project.repository.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StripeServiceImplementationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArrangementInOrderRepository arrangementInOrderRepository;

    @Mock
    private ArrangementRepository arrangementRepository;

    @Mock
    private ArrangementInShoppingCartRepository arrangementInShoppingCartRepository;

    @InjectMocks
    private StripeServiceImplementation stripeService;

    @Spy
    private Map<String, Object> chargeParams = new HashMap<>();

    private User user;
    private Order order;
    private Arrangement arrangement;
    private ArrangementInOrder arrangementInOrder;

    @Before
    public void setup() {
        user = new User();
        order = new Order();
        arrangement = new Arrangement();
        arrangementInOrder = new ArrangementInOrder();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Optional.of(order));
        when(arrangementRepository.findById(arrangement.getId())).thenReturn(Optional.of(arrangement));
        when(orderRepository.save(order)).thenReturn(order);
        when(arrangementInOrderRepository.save(arrangementInOrder)).thenReturn(arrangementInOrder);
    }

    @Test
    public void createCharge_ShouldReturnCharge()throws StripeException {
//        ChargeRequest chargeRequest = new ChargeRequest();
//        chargeRequest.setStripeToken("sk_test_51MX80hIsnUFJ6fztzS6nAyzhPNu6ieywbmmJ49rDkddKwI3GYeigjjOO02oPnzlcE2QI8CIjdBaQ3vJDFbTzEZ5p00zHcnU6B1");
//        Charge expectedCharge = new Charge();
//
//        when(arrangementInOrderRepository.findByOrderAndFromDateAfter(order, LocalDate.now())).thenReturn(Collections.singletonList(arrangementInOrder));
//
//        Map<String, Object> chargeParams = new HashMap<>();
//        chargeParams.put("amount", 100);
//        chargeParams.put("currency", "eur");
//        chargeParams.put("description", "someDescription");
//        chargeParams.put("source", "tok_1MaUxK2eZvKYlo2CpQMHkkHs");
//        when(Charge.create(chargeParams)).thenReturn(expectedCharge);
//
//        Charge resultCharge = stripeService.createCharge(chargeRequest);
//
//        verify(userRepository, times(1)).findByUsername(user.getUsername());
//        verify(orderRepository, times(1)).findByUser(user);
//        verify(arrangementRepository, times(1)).findById(arrangement.getId());
//        verify(arrangementInOrderRepository, times(1)).findByOrderAndFromDateAfter(order, LocalDate.now());
//        verify(orderRepository, times(2)).save(order);
//        verify(arrangementInOrderRepository, times(1)).save(arrangementInOrder);
//        verify(arrangementInShoppingCartRepository, times(1)).deleteById(chargeRequest.getArrangement());
//        verify(Charge.create(anyMap()), times(1)).create(anyMap());
//
//        assertEquals(expectedCharge, resultCharge);
  //
//        User user = new User();
//        user.setUsername("user1");
//        Order order = new Order(user);
//        Arrangement arrangement = new Arrangement(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 1), null);
//        ArrangementInOrder arrangementInOrder = new ArrangementInOrder(arrangement, order, arrangement.getFrom_date(), arrangement.getTo_date(), 100.0);
//        ChargeRequest chargeRequest = new ChargeRequest(100, "USD", "description", "token", "user1", arrangement.getId());
//        when(userRepository.findByUsername(chargeRequest.getUser())).thenReturn(Optional.of(user));
//        when(arrangementRepository.findById(chargeRequest.getArrangement())).thenReturn(Optional.of(arrangement));
//        when(orderRepository.findByUser(user)).thenReturn(Optional.of(order));
//        when(arrangementInOrderRepository.save(arrangementInOrder)).thenReturn(arrangementInOrder);
//        Charge expectedCharge = mock(Charge.class);
//        Map<String, Object> chargeParams = new HashMap<>();
//        chargeParams.put("amount", chargeRequest.getAmount());
//        chargeParams.put("currency", chargeRequest.getCurrency());
//        chargeParams.put("description", chargeRequest.getDescription());
//        chargeParams.put("source", chargeRequest.getStripeToken());
//        when(Charge.create(chargeParams)).thenReturn(expectedCharge);
//
//        // When
//        Charge result = stripeService.createCharge(chargeRequest);
//
//        // Then
//        verify(userRepository).findByUsername(chargeRequest.getUser());
//        verify(arrangementRepository).findById(chargeRequest.getArrangement());
//        verify(orderRepository).findByUser(user);
//        verify(arrangementInOrderRepository).save(arrangementInOrder);
//        verify(arrangementInShoppingCartRepository).deleteById(chargeRequest.getArrangement());
//        verify(orderRepository).save(order);
//        assertEquals(expectedCharge, result);
//        assertEquals(100.0, order.getTotalCost(), 0.01);
//        assertEquals(arrangementInOrder, arrangementInOrderRepository.findByOrderAndFromDateAfter(order, LocalDate.now()).get(0));
    }

}