package com.project.service.implementation;

import com.project.domain.Order;
import com.project.domain.exceptions.OrderNotFoundException;
import com.project.domain.exceptions.UserNotFoundException;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInOrder;
import com.project.repository.ArrangementInOrderRepository;
import com.project.repository.OrderRepository;
import com.project.repository.UserRepository;
import com.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final ArrangementInOrderRepository arrangementInOrderRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<ArrangementInOrder> findAllByOrderAndFutureArrangement(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Order order = this.orderRepository.findByUser(user).orElseThrow(OrderNotFoundException::new);
        return this.arrangementInOrderRepository.findByOrderAndFromDateAfter(order, LocalDate.now());
    }

    @Override
    public Double getTotalPrice(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Order order = this.orderRepository.findByUser(user).orElseThrow(OrderNotFoundException::new);
        return order.getTotalCost();
    }
}
