package com.project.service.implementation;

import com.project.domain.Arrangement;
import com.project.domain.Order;
import com.project.domain.dto.ChargeRequest;
import com.project.domain.exceptions.ArrangementNotFoundException;
import com.project.domain.exceptions.UserNotFoundException;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInOrder;
import com.project.repository.*;
import com.project.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeServiceImplementation implements StripeService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ArrangementInOrderRepository arrangementInOrderRepository;
    private final ArrangementRepository arrangementRepository;
    private final ArrangementInShoppingCartRepository arrangementInShoppingCartRepository;

    public StripeServiceImplementation(OrderRepository orderRepository,
                                       UserRepository userRepository,
                                       ArrangementInOrderRepository arrangementInOrderRepository,
                                       ArrangementRepository arrangementRepository,
                                       ArrangementInShoppingCartRepository arrangementInShoppingCartRepository) {
        Stripe.apiKey = "sk_test_51MX80hIsnUFJ6fztzS6nAyzhPNu6ieywbmmJ49rDkddKwI3GYeigjjOO02oPnzlcE2QI8CIjdBaQ3vJDFbTzEZ5p00zHcnU6B1";
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.arrangementInOrderRepository = arrangementInOrderRepository;
        this.arrangementRepository = arrangementRepository;
        this.arrangementInShoppingCartRepository = arrangementInShoppingCartRepository;
    }

    @Override
    public Charge createCharge(ChargeRequest chargeRequest) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        User user = this.userRepository.findByUsername(chargeRequest.getUser()).orElseThrow(UserNotFoundException::new);
        Order order = this.orderRepository.findByUser(user).orElseGet(() -> this.orderRepository.save(new Order(user)));
        Arrangement arrangement = this.arrangementRepository.findById(chargeRequest.getArrangement()).orElseThrow(ArrangementNotFoundException::new);
        ArrangementInOrder arrangementInOrder = new ArrangementInOrder(arrangement, order, arrangement.getFrom_date(), arrangement.getTo_date(), chargeRequest.getAmount().doubleValue());
        this.arrangementInOrderRepository.save(arrangementInOrder);
        double totalCost = this.arrangementInOrderRepository.findByOrderAndFromDateAfter(order, LocalDate.now())
                .stream().mapToDouble(ArrangementInOrder::getPrice).sum();
        order.setTotalCost(totalCost);
        this.orderRepository.save(order);
        this.arrangementInShoppingCartRepository.deleteById(chargeRequest.getArrangement());

        return Charge.create(chargeParams);
    }
}
