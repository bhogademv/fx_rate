package com.arenema.fx_rate.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arenema.fx_rate.model.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findTop3ByTargetCurrencyOrderByDateDesc(String targetCurrency);
    List<ExchangeRate> findByDateAndTargetCurrency(LocalDate date, String targetCurrency);
}
