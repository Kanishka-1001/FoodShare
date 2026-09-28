package com.sece.foodshare.repository;

import com.sece.foodshare.entity.FoodListing;
import com.sece.foodshare.entity.ListingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FoodListingRepository
        extends JpaRepository<FoodListing, Long> {

    List<FoodListing> findByStatusAndSafeUntilAfterOrderBySafeUntilAsc(
            ListingStatus status,
            LocalDateTime time
    );

    List<FoodListing> findByStatusInAndSafeUntilBefore(
            List<ListingStatus> statuses,
            LocalDateTime time
    );

    List<FoodListing> findAllByOrderByCreatedAtDesc();

    List<FoodListing> findByDonorIdOrderByCreatedAtDesc(Long donorId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM FoodListing f WHERE f.id = :id")
    Optional<FoodListing> findByIdForUpdate(@Param("id") Long id);
}