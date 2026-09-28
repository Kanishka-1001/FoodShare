package com.sece.foodshare.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime claimedAt;

    private LocalDateTime collectedAt;

    private LocalDateTime cancelledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_listing_id", nullable = false)
    private FoodListing foodListing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", nullable = false)
    private NGO ngo;

    public Claim() {
    }

    @PrePersist
    public void onCreate() {
        if (claimedAt == null) {
            claimedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = ClaimStatus.ACTIVE;
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getClaimedAt() {
        return claimedAt;
    }

    public void setClaimedAt(LocalDateTime claimedAt){
        this.claimedAt=claimedAt;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public FoodListing getFoodListing() {
        return foodListing;
    }

    public void setFoodListing(FoodListing foodListing) {
        this.foodListing = foodListing;
    }

    public NGO getNgo() {
        return ngo;
    }

    public void setNgo(NGO ngo) {
        this.ngo = ngo;
    }
}