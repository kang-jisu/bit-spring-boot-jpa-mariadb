package com.jisu.api.item.service;

import com.jisu.api.item.domain.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ItemService {
    List<Item> findAll();

    Optional<Item> findById(Long id);

    void save(Item item);

    boolean existsById(Long id);

    Long count();

    void deleteAll();

    void deleteById(Long id);
}
