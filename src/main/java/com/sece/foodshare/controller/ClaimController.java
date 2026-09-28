package com.sece.foodshare.controller;

import com.sece.foodshare.dto.request.ClaimRequest;
import com.sece.foodshare.dto.response.ClaimResponse;
import com.sece.foodshare.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(
            @Valid @RequestBody ClaimRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(claimService.createClaim(request));
    }

    @PutMapping("/{id}/collect")
    public ResponseEntity<ClaimResponse> collect(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                claimService.collectClaim(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAll() {

        return ResponseEntity.ok(
                claimService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                claimService.getById(id)
        );
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<ClaimResponse>> getByNgo(
            @PathVariable Long ngoId) {

        return ResponseEntity.ok(
                claimService.getByNgo(ngoId)
        );
    }
}