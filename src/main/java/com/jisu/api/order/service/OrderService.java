package com.jisu.api.order.service;

import com.jisu.api.order.domain.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface OrderService {
    List<Order> findAll();

    Optional<Order> findById(Long id);

    void save(Order order);

    boolean existsById(Long id);

    Long count();

    void deleteById(Long id);
}
