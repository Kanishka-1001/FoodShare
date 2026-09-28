package com.sece.foodshare.service;

import com.sece.foodshare.dto.request.DonorRequest;
import com.sece.foodshare.dto.response.DonorResponse;
import com.sece.foodshare.entity.Donor;
import com.sece.foodshare.exception.BusinessRuleException;
import com.sece.foodshare.exception.ResourceNotFoundException;
import com.sece.foodshare.repository.DonorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonorService {

    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @Transactional
    public DonorResponse create(DonorRequest request) {

        if (donorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException(
                    "A donor with this email already exists"
            );
        }

        Donor donor = new Donor();

        donor.setName(request.getName());
        donor.setEmail(request.getEmail());
        donor.setPhone(request.getPhone());
        donor.setOrganization(request.getOrganization());

        Donor saved = donorRepository.save(donor);

        return toResponse(saved);
    }

    public List<DonorResponse> getAll() {

        return donorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DonorResponse getById(Long id) {

        Donor donor = donorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor not found with ID: " + id
                        )
                );

        return toResponse(donor);
    }

    @Transactional
    public DonorResponse update(
            Long id,
            DonorRequest request) {

        Donor donor = donorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor not found with ID: " + id
                        )
                );

        donor.setName(request.getName());
        donor.setEmail(request.getEmail());
        donor.setPhone(request.getPhone());
        donor.setOrganization(request.getOrganization());

        return toResponse(donorRepository.save(donor));
    }

    @Transactional
    public void delete(Long id) {

        Donor donor = donorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor not found with ID: " + id
                        )
                );

        donorRepository.delete(donor);
    }

    private DonorResponse toResponse(Donor donor) {

        return new DonorResponse(
                donor.getId(),
                donor.getName(),
                donor.getEmail(),
                donor.getPhone(),
                donor.getOrganization()
        );
    }
}