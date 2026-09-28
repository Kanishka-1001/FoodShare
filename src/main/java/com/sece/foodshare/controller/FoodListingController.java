package com.sece.foodshare.controller;

import com.sece.foodshare.dto.request.FoodListingRequest;
import com.sece.foodshare.dto.response.FoodListingResponse;
import com.sece.foodshare.service.FoodListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-listings")
public class FoodListingController {

    private final FoodListingService foodListingService;

    public FoodListingController(
            FoodListingService foodListingService) {

        this.foodListingService = foodListingService;
    }

    @PostMapping
    public ResponseEntity<FoodListingResponse> create(
            @Valid @RequestBody FoodListingRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(foodListingService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<FoodListingResponse>> getAll() {

        return ResponseEntity.ok(
                foodListingService.getAll()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<FoodListingResponse>>
    getAvailable() {

        return ResponseEntity.ok(
                foodListingService.getAvailable()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodListingResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                foodListingService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodListingResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FoodListingRequest request) {

        return ResponseEntity.ok(
                foodListingService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        foodListingService.delete(id);

        return ResponseEntity.noContent().build();
    }
}