package com.project.service.implementation;

import com.project.domain.Order;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInOrder;
import com.project.repository.ArrangementInOrderRepository;
import com.project.repository.OrderRepository;
import com.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplementationTest {

    @Mock
    private ArrangementInOrderRepository arrangementInOrderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImplementation orderService;

    @Test
    public void findAllByOrderAndFutureArrangement_ShouldReturnListOfArrangementInOrder() {
        String username = "testuser";
        User user = new User();
        Order order = new Order();
        ArrangementInOrder arrangementInOrder = new ArrangementInOrder();
        List<ArrangementInOrder> arrangementInOrderList = Collections.singletonList(arrangementInOrder);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Optional.of(order));
        when(arrangementInOrderRepository.findByOrderAndFromDateAfter(order, LocalDate.now())).thenReturn(arrangementInOrderList);

        List<ArrangementInOrder> returnedArrangementInOrderList = orderService.findAllByOrderAndFutureArrangement(username);

        assertEquals(arrangementInOrderList, returnedArrangementInOrderList);
    }

    @Test
    public void getTotalPrice_ShouldReturnDouble() {
        String username = "testuser";
        User user = new User();
        Order order = mock(Order.class);
        Double totalCost = 10.0;

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Optional.of(order));
        when(order.getTotalCost()).thenReturn(totalCost);

        Double returnedTotalCost = orderService.getTotalPrice(username);

        assertEquals(totalCost, returnedTotalCost);
    }


}