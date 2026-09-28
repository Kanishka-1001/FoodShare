package com.sece.foodshare.dto.response;

import com.sece.foodshare.entity.ListingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FoodListingResponse {

    private Long id;
    private String foodName;
    private String foodType;
    private BigDecimal quantity;
    private String unit;
    private LocalDateTime safeUntil;
    private String pickupLocation;
    private ListingStatus status;

    private Long donorId;
    private String donorName;

    public FoodListingResponse(Long id,
                               String foodName,
                               String foodType,
                               BigDecimal quantity,
                               String unit,
                               LocalDateTime safeUntil,
                               String pickupLocation,
                               ListingStatus status,
                               Long donorId,
                               String donorName) {

        this.id = id;
        this.foodName = foodName;
        this.foodType = foodType;
        this.quantity = quantity;
        this.unit = unit;
        this.safeUntil = safeUntil;
        this.pickupLocation = pickupLocation;
        this.status = status;
        this.donorId = donorId;
        this.donorName = donorName;
    }

    public Long getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDateTime getSafeUntil() {
        return safeUntil;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public Long getDonorId() {
        return donorId;
    }

    public String getDonorName() {
        return donorName;
    }
}