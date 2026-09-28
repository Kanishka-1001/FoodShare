package com.sece.foodshare.service;

import com.sece.foodshare.dto.request.NGORequest;
import com.sece.foodshare.dto.response.NGOResponse;
import com.sece.foodshare.entity.NGO;
import com.sece.foodshare.exception.BusinessRuleException;
import com.sece.foodshare.exception.ResourceNotFoundException;
import com.sece.foodshare.repository.NGORepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NGOService {

    private final NGORepository ngoRepository;

    public NGOService(NGORepository ngoRepository) {
        this.ngoRepository = ngoRepository;
    }

    @Transactional
    public NGOResponse create(NGORequest request) {

        if (ngoRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException(
                    "A donor with this email already exists"
            );
        }

        NGO ngo = new NGO();

        ngo.setName(request.getName());
        ngo.setEmail(request.getEmail());
        ngo.setPhone(request.getPhone());
        ngo.setAddress(request.getAddress());

        return toResponse(ngoRepository.save(ngo));
    }

    public List<NGOResponse> getAll() {

        return ngoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NGOResponse getById(Long id) {

        NGO ngo = ngoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NGO not found with ID: " + id
                        )
                );

        return toResponse(ngo);
    }

    @Transactional
    public NGOResponse update(
            Long id,
            NGORequest request) {

        NGO ngo = ngoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NGO not found with ID: " + id
                        )
                );

        ngo.setName(request.getName());
        ngo.setEmail(request.getEmail());
        ngo.setPhone(request.getPhone());
        ngo.setAddress(request.getAddress());

        return toResponse(ngoRepository.save(ngo));
    }

    @Transactional
    public void delete(Long id) {

        NGO ngo = ngoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NGO not found with ID: " + id
                        )
                );

        ngoRepository.delete(ngo);
    }

    private NGOResponse toResponse(NGO ngo) {

        return new NGOResponse(
                ngo.getId(),
                ngo.getName(),
                ngo.getEmail(),
                ngo.getPhone(),
                ngo.getAddress()
        );
    }
}