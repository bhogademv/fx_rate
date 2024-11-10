package com.arenema.fx_rate.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arenema.fx_rate.entity.ExchangeRate;
import com.arenema.fx_rate.repo.ExchangeRateRepository;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeRate getExchangeRate(String targetCurrency) {
        Optional<ExchangeRate> optionalRate = exchangeRateRepository.findByDateAndTargetCurrency(LocalDate.now(), targetCurrency).stream().findFirst();
        if (optionalRate.isPresent()) {
            return optionalRate.get();
        } else {
            String url = "https://api.frankfurter.app/latest?from=USD&to=" + targetCurrency;
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            if (response != null && response.getRates() != null && response.getRates().containsKey(targetCurrency)) {
                ExchangeRate rate = new ExchangeRate();
                rate.setDate(LocalDate.now());
                rate.setSourceCurrency("USD");
                rate.setTargetCurrency(targetCurrency);
                rate.setRate(response.getRates().get(targetCurrency));
                return exchangeRateRepository.save(rate);
            } else {
                throw new RuntimeException("Failed to fetch exchange rate from external API");
            }
        }
    }

    public List<ExchangeRate> getLatestRates(String targetCurrency) {
        return exchangeRateRepository.findTop3ByTargetCurrencyOrderByDateDesc(targetCurrency);
    }
}
