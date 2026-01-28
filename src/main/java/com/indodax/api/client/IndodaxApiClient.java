package com.indodax.api.client;

import com.indodax.config.ApiConfig;
import io.restassured.response.Response;

import java.util.Map;

public class IndodaxApiClient extends BaseApiClient {

    public IndodaxApiClient() {
        super();
        logger.info("Indodax API Client initialized");
        logger.info("Base URL: {}", ApiConfig.getBaseUrl());
        logger.info("Environment: {}", ApiConfig.getCurrentEnvironment());
    }

    public Response getServerTime() {
        return get("/server_time");
    }

    public Response getTicker(String pair) {
        return get("/ticker/" + pair);
    }

    public Response getAllTickers() {
        return get("/tickers");
    }

    public Response getPairs() {
        return get("/pairs");
    }

    public Response getPriceIncrements() {
        return get("/price_increments");
    }

    public Response getSummaries() {
        return get("/summaries");
    }

    public Response getTradeHistory(String pair) {
        return get("/trades/" + pair);
    }

    public Response getDepth(String pair) {
        return get("/depth/" + pair);
    }

    public Response sendGet(String endpoint) {
        return get(endpoint);
    }

    public Response sendPost(String endpoint, Map<String, Object> body) {
        return post(endpoint, body);
    }

    public Response sendPut(String endpoint, Map<String, Object> body) {
        return put(endpoint, body);
    }

    public Response sendPatch(String endpoint, Map<String, Object> body) {
        return patch(endpoint, body);
    }

    public Response sendDelete(String endpoint) {
        return delete(endpoint);
    }

    public void storeDataFromResponse(String key, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        storeValue(key, value);
    }

    public Object getChainedData(String key) {
        return getStoredValue(key);
    }

    public static String getCurrentEnvironment() {
        return ApiConfig.getBaseUrl();
    }

    public Response getLastResponse() {
        return getResponse();
    }
}
