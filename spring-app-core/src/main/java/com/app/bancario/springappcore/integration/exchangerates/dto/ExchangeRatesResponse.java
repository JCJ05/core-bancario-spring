package com.app.bancario.springappcore.integration.exchangerates.dto;

import java.util.Map;

public class ExchangeRatesResponse {
    
    private String base;
    private String date;
    private Map<String,Object> rates;
    private boolean success;
    private long timestamp;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String,Object> getRates() {
        return rates;
    }

    public void setRates(Map<String,Object> rates) {
        this.rates = rates;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
