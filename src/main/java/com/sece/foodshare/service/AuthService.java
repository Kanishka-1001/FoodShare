package com.sece.foodshare.service;

import com.sece.foodshare.dto.response.MonthlySummaryResponse;
import com.sece.foodshare.dto.response.UnitTotalResponse;
import com.sece.foodshare.entity.ClaimStatus;
import com.sece.foodshare.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final ClaimRepository claimRepository;

    public DashboardService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public MonthlySummaryResponse getMonthlySummary(
            int year,
            int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDateTime start =
                yearMonth.atDay(1).atStartOfDay();

        LocalDateTime end =
                yearMonth.plusMonths(1)
                        .atDay(1)
                        .atStartOfDay();

        List<Object[]> results =
                claimRepository.getMonthlyTotals(
                        ClaimStatus.COLLECTED,
                        start,
                        end
                );

        List<UnitTotalResponse> totals =
                new ArrayList<>();

        for (Object[] row : results) {

            String unit = (String) row[0];

            BigDecimal total =
                    (BigDecimal) row[1];

            totals.add(
                    new UnitTotalResponse(
                            unit,
                            total
                    )
            );
        }

        return new MonthlySummaryResponse(
                year,
                month,
                totals
        );
    }
}