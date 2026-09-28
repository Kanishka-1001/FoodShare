package com.sece.foodshare.dto.response;

import java.util.List;

public class MonthlySummaryResponse {

    private int year;
    private int month;
    private List<UnitTotalResponse> totals;

    public MonthlySummaryResponse(int year,
                                  int month,
                                  List<UnitTotalResponse> totals) {
        this.year = year;
        this.month = month;
        this.totals = totals;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public List<UnitTotalResponse> getTotals() {
        return totals;
    }
}