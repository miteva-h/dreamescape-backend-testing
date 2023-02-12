package com.project.service;

import com.project.domain.relations.ArrangementInOrder;

import java.util.List;

public interface OrderService {

    List<ArrangementInOrder> findAllByOrderAndFutureArrangement(String username);

    Double getTotalPrice(String username);
}
