package com.arenema.fx_rate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arenema.fx_rate.entity.ExchangeRate;
import com.arenema.fx_rate.service.ExchangeRateService;

@RestController
@RequestMapping("/fx")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping
    public ExchangeRate getExchangeRate(@RequestParam String targetCurrency) {
        return exchangeRateService.getExchangeRate(targetCurrency);
    }

    @GetMapping("/{targetCurrency}")
    public List<ExchangeRate> getLatestRates(@PathVariable String targetCurrency) {
        return exchangeRateService.getLatestRates(targetCurrency);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(RuntimeException e) {
        return e.getMessage();
    }
}
