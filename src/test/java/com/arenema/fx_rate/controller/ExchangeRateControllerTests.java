package com.arenema.fx_rate.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.arenema.fx_rate.service.ExchangeRateService;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    public void testGetExchangeRate() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("date", "2024-08-26");
        mockResponse.put("source", "USD");

        Map<String, String> rate1 = new HashMap<>();
        rate1.put("target", "GBP");
        rate1.put("value", "0.76192");

        Map<String, String> rate2 = new HashMap<>();
        rate2.put("target", "CZK");
        rate2.put("value", "22.55");

        mockResponse.put("rates", Arrays.asList(rate1, rate2));

        when(exchangeRateService.getExchangeRate("USD")).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/fx?targetCurrency=USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2024-08-26"))
                .andExpect(jsonPath("$.source").value("USD"))
                .andExpect(jsonPath("$.rates[0].target").value("GBP"))
                .andExpect(jsonPath("$.rates[0].value").value("0.76192"))
                .andExpect(jsonPath("$.rates[1].target").value("CZK"))
                .andExpect(jsonPath("$.rates[1].value").value("22.55"));
    }

    @Test
    public void testGetLatestRates() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("source", "USD");

        Map<String, String> rate1 = new HashMap<>();
        rate1.put("target", "GBP");
        rate1.put("value", "0.77206");

        Map<String, String> rate2 = new HashMap<>();
        rate2.put("target", "GBP");
        rate2.put("value", "0.77206");

        Map<String, String> rate3 = new HashMap<>();
        rate3.put("target", "GBP");
        rate3.put("value", "0.77431");

        Map<String, Map<String, String>> ratesMap = new HashMap<>();
        ratesMap.put("2024-08-20", rate1);
        ratesMap.put("2024-08-19", rate2);
        ratesMap.put("2024-08-18", rate3);

        mockResponse.put("rates", ratesMap);

        when(exchangeRateService.getLatestRates("GBP")).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/fx/GBP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("USD"))
                .andExpect(jsonPath("$.rates['2024-08-20'].target").value("GBP"))
                .andExpect(jsonPath("$.rates['2024-08-20'].value").value("0.77206"))
                .andExpect(jsonPath("$.rates['2024-08-19'].target").value("GBP"))
                .andExpect(jsonPath("$.rates['2024-08-19'].value").value("0.77206"))
                .andExpect(jsonPath("$.rates['2024-08-18'].target").value("GBP"))
                .andExpect(jsonPath("$.rates['2024-08-18'].value").value("0.77431"));
    }
}
