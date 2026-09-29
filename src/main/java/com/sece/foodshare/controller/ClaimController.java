package com.sece.foodshare.controller;

import com.sece.foodshare.dto.ClaimRequest;
import com.sece.foodshare.dto.ClaimResponse;
import com.sece.foodshare.service.ClaimService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService service;

    public ClaimController(
            ClaimService service) {

        this.service = service;
    }

    @PostMapping
    public ClaimResponse create(
            @Valid @RequestBody ClaimRequest request,
            HttpSession session) {

        return service.create(
                request,
                session
        );
    }

    @GetMapping("/mine")
    public List<ClaimResponse> mine(
            HttpSession session) {

        return service.myClaims(session);
    }

    @GetMapping("/my-listings")
    public List<ClaimResponse> myListings(
            HttpSession session) {

        return service.claimsForMyListings(
                session
        );
    }

    @GetMapping
    public List<ClaimResponse> all() {

        return service.all();
    }

    @PutMapping("/{id}/collect")
    public ClaimResponse collect(
            @PathVariable Long id,
            HttpSession session) {

        return service.collect(
                id,
                session
        );
    }
}