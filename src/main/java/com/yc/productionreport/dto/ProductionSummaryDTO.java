package com.yc.productionreport.dto;

import java.math.BigDecimal;

/**
 * 生产报表统计数据DTO
 */
public class ProductionSummaryDTO {
    private Integer totalProduction;
    private BigDecimal averageDailyProduction;
    private Integer totalDefects;
    private BigDecimal averageDefectRate;

    // Getters and Setters
    public Integer getTotalProduction() {
        return totalProduction;
    }

    public void setTotalProduction(Integer totalProduction) {
        this.totalProduction = totalProduction;
    }

    public BigDecimal getAverageDailyProduction() {
        return averageDailyProduction;
    }

    public void setAverageDailyProduction(BigDecimal averageDailyProduction) {
        this.averageDailyProduction = averageDailyProduction;
    }

    public Integer getTotalDefects() {
        return totalDefects;
    }

    public void setTotalDefects(Integer totalDefects) {
        this.totalDefects = totalDefects;
    }

    public BigDecimal getAverageDefectRate() {
        return averageDefectRate;
    }

    public void setAverageDefectRate(BigDecimal averageDefectRate) {
        this.averageDefectRate = averageDefectRate;
    }
}