package com.sece.foodshare.controller;

import com.sece.foodshare.dto.response.MonthlySummaryResponse;
import com.sece.foodshare.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlySummaryResponse>
    getMonthlySummary(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        LocalDate today = LocalDate.now();

        if (year == null) {
            year = today.getYear();
        }

        if (month == null) {
            month = today.getMonthValue();
        }

        return ResponseEntity.ok(
                dashboardService.getMonthlySummary(
                        year,
                        month
                )
        );
    }
}