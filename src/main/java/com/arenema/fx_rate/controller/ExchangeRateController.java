package com.arenema.fx_rate.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arenema.fx_rate.service.ExchangeRateService;

@RestController
@RequestMapping("/fx")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping
    public Map<String, Object> getExchangeRate(@RequestParam String targetCurrency) {
        return exchangeRateService.getExchangeRate(targetCurrency);
    }

    @GetMapping("/{targetCurrency}")
    public Map<String, Object> getLatestRates(@PathVariable String targetCurrency) {
        return exchangeRateService.getLatestRates(targetCurrency);
    }
}
