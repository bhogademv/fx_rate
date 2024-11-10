package com.arenema.fx_rate.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ExchangeRateResponse {
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;

    // Getters and setters

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
