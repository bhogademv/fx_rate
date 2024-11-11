package com.arenema.fx_rate.service; 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arenema.fx_rate.model.ExchangeRate;
import com.arenema.fx_rate.repository.ExchangeRateRepository;



@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getExchangeRate(String targetCurrency) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> ratesList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        Optional<ExchangeRate> optionalRate = exchangeRateRepository.findByDateAndTargetCurrency(today, targetCurrency).stream().findFirst();
        if (optionalRate.isPresent()) {
            ExchangeRate rate = optionalRate.get();
            response.put("date", rate.getDate().toString());
            response.put("source", rate.getSourceCurrency());

            Map<String, String> rateDetails = new HashMap<>();
            rateDetails.put("target", rate.getTargetCurrency());
            rateDetails.put("value", rate.getRate().toString());
            ratesList.add(rateDetails);

        } else {
            String url = "https://api.frankfurter.app/latest?from=USD&to=" + targetCurrency;
            ExchangeRateResponse apiResponse = restTemplate.getForObject(url, ExchangeRateResponse.class);

            if (apiResponse != null && apiResponse.getRates() != null && apiResponse.getRates().containsKey(targetCurrency)) {
                response.put("date", apiResponse.getDate().toString());
                response.put("source", apiResponse.getBase());

                Map<String, String> rateDetails = new HashMap<>();
                rateDetails.put("target", targetCurrency);
                rateDetails.put("value", apiResponse.getRates().get(targetCurrency).toString());
                ratesList.add(rateDetails);

                ExchangeRate rate = new ExchangeRate();
                rate.setDate(LocalDate.now());
                rate.setSourceCurrency("USD");
                rate.setTargetCurrency(targetCurrency);
                rate.setRate(apiResponse.getRates().get(targetCurrency));
                exchangeRateRepository.save(rate);
            }
        }
        response.put("rates", ratesList);
        return response;
    }

    public Map<String, Object> getLatestRates(String targetCurrency) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Map<String, String>> ratesMap = new LinkedHashMap<>();

        List<ExchangeRate> rates = exchangeRateRepository.findTop3ByTargetCurrencyOrderByDateDesc(targetCurrency);
        response.put("source", "USD");

        for (ExchangeRate rate : rates) {
            Map<String, String> rateDetails = new HashMap<>();
            rateDetails.put("target", rate.getTargetCurrency());
            rateDetails.put("value", rate.getRate().toString());
            ratesMap.put(rate.getDate().toString(), rateDetails);
        }

        response.put("rates", ratesMap);
        return response;
    }
}