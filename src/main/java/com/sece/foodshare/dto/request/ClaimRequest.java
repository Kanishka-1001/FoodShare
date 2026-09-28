package com.sece.foodshare.dto.request;

import jakarta.validation.constraints.NotNull;

public class ClaimRequest {

    @NotNull(message = "Food listing ID is required")
    private Long foodListingId;

    @NotNull(message = "NGO ID is required")
    private Long ngoId;

    public Long getFoodListingId() {
        return foodListingId;
    }

    public void setFoodListingId(Long foodListingId) {
        this.foodListingId = foodListingId;
    }

    public Long getNgoId() {
        return ngoId;
    }

    public void setNgoId(Long ngoId) {
        this.ngoId = ngoId;
    }
}