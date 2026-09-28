package com.sece.foodshare.service;

import com.sece.foodshare.dto.request.FoodListingRequest;
import com.sece.foodshare.dto.response.FoodListingResponse;
import com.sece.foodshare.entity.Donor;
import com.sece.foodshare.entity.FoodListing;
import com.sece.foodshare.entity.ListingStatus;
import com.sece.foodshare.exception.BusinessRuleException;
import com.sece.foodshare.exception.ResourceNotFoundException;
import com.sece.foodshare.repository.ClaimRepository;
import com.sece.foodshare.repository.DonorRepository;
import com.sece.foodshare.repository.FoodListingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodListingService {

    private final FoodListingRepository foodListingRepository;
    private final DonorRepository donorRepository;
    private final ClaimRepository claimRepository;

    public FoodListingService(
            FoodListingRepository foodListingRepository,
            DonorRepository donorRepository,
            ClaimRepository claimRepository) {

        this.foodListingRepository = foodListingRepository;
        this.donorRepository = donorRepository;
        this.claimRepository = claimRepository;
    }

    @Transactional
    public FoodListingResponse create(FoodListingRequest request) {

        Donor donor = donorRepository.findById(request.getDonorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor not found with ID: "
                                        + request.getDonorId()
                        )
                );

        FoodListing listing = new FoodListing();

        listing.setFoodName(request.getFoodName());
        listing.setFoodType(request.getFoodType());
        listing.setQuantity(request.getQuantity());
        listing.setUnit(request.getUnit());
        listing.setSafeUntil(request.getSafeUntil());
        listing.setPickupLocation(request.getPickupLocation());
        listing.setDonor(donor);
        listing.setStatus(ListingStatus.AVAILABLE);

        return toResponse(foodListingRepository.save(listing));
    }

    @Transactional
    public List<FoodListingResponse> getAll() {

        expireListings();

        return foodListingRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<FoodListingResponse> getAvailable() {

        expireListings();

        return foodListingRepository
                .findByStatusAndSafeUntilAfterOrderBySafeUntilAsc(
                        ListingStatus.AVAILABLE,
                        LocalDateTime.now()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FoodListingResponse getById(Long id) {

        expireListings();

        FoodListing listing = foodListingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food listing not found with ID: " + id
                        )
                );

        return toResponse(listing);
    }

    @Transactional
    public FoodListingResponse update(
            Long id,
            FoodListingRequest request) {

        FoodListing listing =
                foodListingRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Food listing not found with ID: "
                                                + id
                                )
                        );

        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw new BusinessRuleException(
                    "Only an available listing can be updated."
            );
        }

        if (!request.getDonorId()
                .equals(listing.getDonor().getId())) {

            Donor donor = donorRepository
                    .findById(request.getDonorId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Donor not found."
                            ));

            listing.setDonor(donor);
        }

        listing.setFoodName(request.getFoodName());
        listing.setFoodType(request.getFoodType());
        listing.setQuantity(request.getQuantity());
        listing.setUnit(request.getUnit());
        listing.setSafeUntil(request.getSafeUntil());
        listing.setPickupLocation(request.getPickupLocation());

        return toResponse(
                foodListingRepository.save(listing)
        );
    }

    @Transactional
    public void delete(Long id) {

        FoodListing listing =
                foodListingRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Food listing not found with ID: "
                                                + id
                                )
                        );

        if (listing.getStatus() == ListingStatus.CLAIMED) {
            throw new BusinessRuleException(
                    "A claimed listing cannot be deleted."
            );
        }

        if (listing.getStatus() == ListingStatus.COLLECTED) {
            throw new BusinessRuleException(
                    "A collected listing cannot be deleted."
            );
        }

        foodListingRepository.delete(listing);
    }

    /**
     * Automatically expires listings whose safeUntil time has passed.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireListings() {

        LocalDateTime now = LocalDateTime.now();

        List<FoodListing> expiredListings =
                foodListingRepository
                        .findByStatusInAndSafeUntilBefore(
                                List.of(
                                        ListingStatus.AVAILABLE,
                                        ListingStatus.CLAIMED
                                ),
                                now
                        );

        for (FoodListing listing : expiredListings) {

            listing.setStatus(ListingStatus.EXPIRED);

            claimRepository
                    .findByFoodListingIdAndStatus(
                            listing.getId(),
                            com.sece.foodshare.entity.ClaimStatus.ACTIVE
                    )
                    .forEach(claim -> {
                        claim.setStatus(
                                com.sece.foodshare.entity.ClaimStatus.CANCELLED
                        );
                        claim.setCancelledAt(now);
                    });
        }

        foodListingRepository.saveAll(expiredListings);
    }

    private FoodListingResponse toResponse(FoodListing listing) {

        return new FoodListingResponse(
                listing.getId(),
                listing.getFoodName(),
                listing.getFoodType(),
                listing.getQuantity(),
                listing.getUnit(),
                listing.getSafeUntil(),
                listing.getPickupLocation(),
                listing.getStatus(),
                listing.getDonor().getId(),
                listing.getDonor().getName()
        );
    }
}