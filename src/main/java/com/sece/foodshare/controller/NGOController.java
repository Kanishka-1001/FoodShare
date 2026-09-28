package com.sece.foodshare.controller;

import com.sece.foodshare.dto.request.NGORequest;
import com.sece.foodshare.dto.response.NGOResponse;
import com.sece.foodshare.service.NGOService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngos")
public class NGOController {

    private final NGOService ngoService;

    public NGOController(NGOService ngoService) {
        this.ngoService = ngoService;
    }

    @PostMapping
    public ResponseEntity<NGOResponse> create(
            @Valid @RequestBody NGORequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ngoService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<NGOResponse>> getAll() {

        return ResponseEntity.ok(
                ngoService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NGOResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ngoService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<NGOResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody NGORequest request) {

        return ResponseEntity.ok(
                ngoService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        ngoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}