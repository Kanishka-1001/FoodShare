package com.sece.foodshare.dto.response;

import com.sece.foodshare.entity.ClaimStatus;

import java.time.LocalDateTime;

public class ClaimResponse {

    private Long id;
    private Long foodListingId;
    private String foodName;

    private Long ngoId;
    private String ngoName;

    private ClaimStatus status;

    private LocalDateTime claimedAt;
    private LocalDateTime collectedAt;

    public ClaimResponse(Long id,
                         Long foodListingId,
                         String foodName,
                         Long ngoId,
                         String ngoName,
                         ClaimStatus status,
                         LocalDateTime claimedAt,
                         LocalDateTime collectedAt) {

        this.id = id;
        this.foodListingId = foodListingId;
        this.foodName = foodName;
        this.ngoId = ngoId;
        this.ngoName = ngoName;
        this.status = status;
        this.claimedAt = claimedAt;
        this.collectedAt = collectedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getFoodListingId() {
        return foodListingId;
    }

    public String getFoodName() {
        return foodName;
    }

    public Long getNgoId() {
        return ngoId;
    }

    public String getNgoName() {
        return ngoName;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public LocalDateTime getClaimedAt() {
        return claimedAt;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }
}