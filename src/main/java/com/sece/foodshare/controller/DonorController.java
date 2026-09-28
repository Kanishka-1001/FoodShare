package com.sece.foodshare.controller;

import com.sece.foodshare.dto.request.DonorRequest;
import com.sece.foodshare.dto.response.DonorResponse;
import com.sece.foodshare.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public ResponseEntity<DonorResponse> create(
            @Valid @RequestBody DonorRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(donorService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DonorResponse>> getAll() {

        return ResponseEntity.ok(
                donorService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                donorService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DonorRequest request) {

        return ResponseEntity.ok(
                donorService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        donorService.delete(id);

        return ResponseEntity.noContent().build();
    }
}