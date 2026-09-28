package com.sece.foodshare.dto.response;

import java.math.BigDecimal;

public class UnitTotalResponse {

    private String unit;
    private BigDecimal totalQuantity;

    public UnitTotalResponse(String unit, BigDecimal totalQuantity) {
        this.unit = unit;
        this.totalQuantity = totalQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }
}