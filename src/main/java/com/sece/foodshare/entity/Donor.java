package com.sece.foodshare.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "donor")
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String organization;

    @OneToMany(mappedBy = "donor")
    private List<FoodListing> foodListings = new ArrayList<>();

    public Donor() {
    }

    public Donor(String name, String email, String phone, String organization) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<FoodListing> getFoodListings() {
        return foodListings;
    }

    public void setFoodListings(List<FoodListing> foodListings) {
        this.foodListings = foodListings;
    }
}