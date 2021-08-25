package com.jisu.api.item.controller;

import com.jisu.api.item.domain.Item;
import com.jisu.api.item.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequestMapping("/items")
public class ItemController {


    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Item>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping
    public void save(@RequestBody Item item) {
        itemService.save(item);
    }

    @PutMapping
    public void update(@RequestBody Item item) {
        itemService.save(item);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.existsById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(itemService.count());
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access Denied"),
            @ApiResponse(code = 422, message = "Username is alredy in use")})
    public void deleteById(@PathVariable Long id) {
        itemService.deleteById(id);
    }
}
