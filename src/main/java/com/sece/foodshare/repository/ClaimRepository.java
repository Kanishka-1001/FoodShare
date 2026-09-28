package com.sece.foodshare.repository;

import com.sece.foodshare.entity.Claim;
import com.sece.foodshare.entity.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    boolean existsByFoodListingIdAndStatus(
            Long foodListingId,
            ClaimStatus status
    );

    List<Claim> findByNgoIdOrderByClaimedAtDesc(Long ngoId);

    List<Claim> findAllByOrderByClaimedAtDesc();

    List<Claim> findByFoodListingIdAndStatus(
            Long foodListingId,
            ClaimStatus status
    );

    @Query("""
            SELECT c
            FROM Claim c
            WHERE c.foodListing.id = :listingId
            AND c.status = :status
            """)
    java.util.Optional<Claim> findActiveClaim(
            @Param("listingId") Long listingId,
            @Param("status") ClaimStatus status
    );

    @Query("""
            SELECT f.unit, SUM(f.quantity)
            FROM Claim c
            JOIN c.foodListing f
            WHERE c.status = :status
            AND c.collectedAt >= :start
            AND c.collectedAt < :end
            GROUP BY f.unit
            """)
    List<Object[]> getMonthlyTotals(
            @Param("status") ClaimStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}