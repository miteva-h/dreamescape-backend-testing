package com.project.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.project.domain.relations.ArrangementInOrder;
import com.project.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void findAllOrders_ShouldReturnListOfOrders() throws Exception {
        String username = "user";
        List<ArrangementInOrder> expectedOrders = List.of(new ArrangementInOrder(), new ArrangementInOrder());

        when(orderService.findAllByOrderAndFutureArrangement(username)).thenReturn(expectedOrders);

        mockMvc.perform(get("/orders/user/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(expectedOrders.size())));
    }

    @Test
    public void getTotalPrice_ShouldReturnDouble() throws Exception {
        String username = "user";
        double expectedTotalPrice = 100.0;

        when(orderService.getTotalPrice(username)).thenReturn(expectedTotalPrice);

        mockMvc.perform(get("/orders/totalPrice/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", is(expectedTotalPrice)));
    }

}
