package com.sece.foodshare.service;

import com.sece.foodshare.dto.request.ClaimRequest;
import com.sece.foodshare.dto.response.ClaimResponse;
import com.sece.foodshare.entity.Claim;
import com.sece.foodshare.entity.ClaimStatus;
import com.sece.foodshare.entity.FoodListing;
import com.sece.foodshare.entity.ListingStatus;
import com.sece.foodshare.entity.NGO;
import com.sece.foodshare.exception.BusinessRuleException;
import com.sece.foodshare.exception.ResourceNotFoundException;
import com.sece.foodshare.repository.ClaimRepository;
import com.sece.foodshare.repository.FoodListingRepository;
import com.sece.foodshare.repository.NGORepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final FoodListingRepository foodListingRepository;
    private final NGORepository ngoRepository;

    public ClaimService(
            ClaimRepository claimRepository,
            FoodListingRepository foodListingRepository,
            NGORepository ngoRepository) {

        this.claimRepository = claimRepository;
        this.foodListingRepository = foodListingRepository;
        this.ngoRepository = ngoRepository;
    }

    @Transactional
    public ClaimResponse createClaim(ClaimRequest request) {

        /*
         * Lock the listing while processing the claim.
         * This helps prevent two NGOs from claiming
         * the same listing simultaneously.
         */
        FoodListing listing =
                foodListingRepository
                        .findByIdForUpdate(
                                request.getFoodListingId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Food listing not found with ID: "
                                                + request.getFoodListingId()
                                )
                        );

        NGO ngo = ngoRepository.findById(request.getNgoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NGO not found with ID: "
                                        + request.getNgoId()
                        )
                );

        LocalDateTime now = LocalDateTime.now();

        // RULE 1
        if (!now.isBefore(listing.getSafeUntil())) {

            listing.setStatus(ListingStatus.EXPIRED);
            foodListingRepository.save(listing);

            throw new BusinessRuleException(
                    "This food listing has expired and can no longer be claimed."
            );
        }

        // RULE 2
        if (listing.getStatus() != ListingStatus.AVAILABLE) {

            if (listing.getStatus() == ListingStatus.CLAIMED) {
                throw new BusinessRuleException(
                        "This listing has already been claimed by another NGO."
                );
            }

            if (listing.getStatus() == ListingStatus.COLLECTED) {
                throw new BusinessRuleException(
                        "This food has already been collected."
                );
            }

            if (listing.getStatus() == ListingStatus.EXPIRED) {
                throw new BusinessRuleException(
                        "This food listing has expired."
                );
            }

            throw new BusinessRuleException(
                    "This listing is not available for claiming."
            );
        }

        // Extra safety check
        if (claimRepository.existsByFoodListingIdAndStatus(
                listing.getId(),
                ClaimStatus.ACTIVE)) {

            throw new BusinessRuleException(
                    "Only one NGO can have an active claim on a listing."
            );
        }

        Claim claim = new Claim();

        claim.setFoodListing(listing);
        claim.setNgo(ngo);
        claim.setStatus(ClaimStatus.ACTIVE);
        claim.setClaimedAt(LocalDateTime.now());

        listing.setStatus(ListingStatus.CLAIMED);

        Claim savedClaim = claimRepository.save(claim);
        foodListingRepository.save(listing);

        return toResponse(savedClaim);
    }

    @Transactional
    public ClaimResponse collectClaim(Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Claim not found with ID: " + claimId
                        )
                );

        if (claim.getStatus() != ClaimStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "Only an active claim can be marked as collected."
            );
        }

        FoodListing listing = claim.getFoodListing();

        LocalDateTime now = LocalDateTime.now();

        if (!now.isBefore(listing.getSafeUntil())) {

            listing.setStatus(ListingStatus.EXPIRED);

            claim.setStatus(ClaimStatus.CANCELLED);
            claim.setCancelledAt(now);

            foodListingRepository.save(listing);
            claimRepository.save(claim);

            throw new BusinessRuleException(
                    "The food has passed its safe-to-eat time and cannot be collected."
            );
        }

        claim.setStatus(ClaimStatus.COLLECTED);
        claim.setCollectedAt(now);

        listing.setStatus(ListingStatus.COLLECTED);

        foodListingRepository.save(listing);

        return toResponse(
                claimRepository.save(claim)
        );
    }

    public List<ClaimResponse> getAll() {

        return claimRepository
                .findAllByOrderByClaimedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ClaimResponse getById(Long id) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Claim not found with ID: " + id
                        )
                );

        return toResponse(claim);
    }

    public List<ClaimResponse> getByNgo(Long ngoId) {

        if (!ngoRepository.existsById(ngoId)) {
            throw new ResourceNotFoundException(
                    "NGO not found with ID: " + ngoId
            );
        }

        return claimRepository
                .findByNgoIdOrderByClaimedAtDesc(ngoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ClaimResponse toResponse(Claim claim) {

        return new ClaimResponse(
                claim.getId(),
                claim.getFoodListing().getId(),
                claim.getFoodListing().getFoodName(),
                claim.getNgo().getId(),
                claim.getNgo().getName(),
                claim.getStatus(),
                claim.getClaimedAt(),
                claim.getCollectedAt()
        );
    }
}