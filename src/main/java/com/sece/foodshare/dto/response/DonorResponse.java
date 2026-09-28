package com.sece.foodshare.dto.response;

public class DonorResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String organization;

    public DonorResponse(Long id, String name, String email,
                         String phone, String organization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.organization = organization;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getOrganization() {
        return organization;
    }
}