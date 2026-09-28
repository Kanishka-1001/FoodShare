package com.sece.foodshare.repository;

import com.sece.foodshare.entity.NGO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NGORepository extends JpaRepository<NGO, Long> {

    Optional<NGO> findByEmail(String email);

    boolean existsByEmail(String email);
}