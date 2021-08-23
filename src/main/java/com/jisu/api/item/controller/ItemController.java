package com.jisu.api.item.controller;

import com.jisu.api.item.domain.Item;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    @GetMapping
    public List<Item> findAll() {
        return null;
    }

    @GetMapping("/{id{")
    public Optional<Item> findById(@PathVariable Long id) {
        return Optional.empty();
    }

    @PostMapping
    public void save(Item item) {

    }

    @PutMapping
    public void update(Item item) {

    }

    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Long id) {
        return false;
    }

    @GetMapping("/count")
    public Long count() {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {

    }
}
