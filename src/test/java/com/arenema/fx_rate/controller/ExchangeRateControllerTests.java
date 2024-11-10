package com.arenema.fx_rate.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.arenema.fx_rate.entity.ExchangeRate;
import com.arenema.fx_rate.service.ExchangeRateService;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    public void testGetExchangeRate() throws Exception {
        ExchangeRate rate = new ExchangeRate();
        rate.setDate(LocalDate.now());
        rate.setSourceCurrency("USD");
        rate.setTargetCurrency("EUR");
        rate.setRate(BigDecimal.valueOf(0.85));

        when(exchangeRateService.getExchangeRate("EUR")).thenReturn(rate);

        mockMvc.perform(MockMvcRequestBuilders.get("/fx?targetCurrency=EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.targetCurrency").value("EUR"))
                .andExpect(jsonPath("$.rate").value(0.85));
    }

    @Test
    public void testGetLatestRates() throws Exception {
        ExchangeRate rate1 = new ExchangeRate();
        rate1.setDate(LocalDate.now().minusDays(2));
        rate1.setSourceCurrency("USD");
        rate1.setTargetCurrency("EUR");
        rate1.setRate(BigDecimal.valueOf(0.84));

        ExchangeRate rate2 = new ExchangeRate();
        rate2.setDate(LocalDate.now().minusDays(1));
        rate2.setSourceCurrency("USD");
        rate2.setTargetCurrency("EUR");
        rate2.setRate(BigDecimal.valueOf(0.85));

        ExchangeRate rate3 = new ExchangeRate();
        rate3.setDate(LocalDate.now());
        rate3.setSourceCurrency("USD");
        rate3.setTargetCurrency("EUR");
        rate3.setRate(BigDecimal.valueOf(0.86));

        when(exchangeRateService.getLatestRates("EUR")).thenReturn(Arrays.asList(rate1, rate2, rate3));

        mockMvc.perform(MockMvcRequestBuilders.get("/fx/EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rate").value(0.84))
                .andExpect(jsonPath("$[1].rate").value(0.85))
                .andExpect(jsonPath("$[2].rate").value(0.86));
    }
}
