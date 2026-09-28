package com.sece.foodshare.repository;

import com.sece.foodshare.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    Optional<Donor> findByEmail(String email);

    boolean existsByEmail(String email);
}