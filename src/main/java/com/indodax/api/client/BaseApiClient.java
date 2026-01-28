package com.indodax.api.client;

import com.indodax.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class BaseApiClient {
    protected static final Logger logger = LoggerFactory.getLogger(BaseApiClient.class);
    protected Response response;
    protected Map<String, Object> chainedData = new HashMap<>();

    public BaseApiClient() {
        RestAssured.baseURI = ApiConfig.getBaseUrl();
        configureTimeouts();
    }

    private void configureTimeouts() {
        RestAssured.config = RestAssured.config()
                .httpClient(io.restassured.config.HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", ApiConfig.CONNECTION_TIMEOUT)
                        .setParam("http.socket.timeout", ApiConfig.SOCKET_TIMEOUT));
    }

    protected RequestSpecification getRequestSpec() {
        RequestSpecification spec = RestAssured.given()
                .header("Accept", ApiConfig.DEFAULT_ACCEPT)
                .header("Content-Type", ApiConfig.DEFAULT_CONTENT_TYPE);

        if (ApiConfig.ENABLE_ALLURE) {
            spec.filter(new AllureRestAssured());
        }

        if (ApiConfig.ENABLE_LOGGING) {
            spec.log().all();
        }

        return spec;
    }

    protected Response get(String endpoint) {
        logger.info("GET {}", endpoint);
        response = getRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    /**
     * Generic POST request with body
     */
    protected Response post(String endpoint, Object body) {
        logger.info("POST {} with body: {}", endpoint, body);
        response = getRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    /**
     * Generic PUT request with body
     */
    protected Response put(String endpoint, Object body) {
        logger.info("PUT {} with body: {}", endpoint, body);
        response = getRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    /**
     * Generic PATCH request with body
     */
    protected Response patch(String endpoint, Object body) {
        logger.info("PATCH {} with body: {}", endpoint, body);
        response = getRequestSpec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    /**
     * Generic DELETE request
     */
    protected Response delete(String endpoint) {
        logger.info("DELETE {}", endpoint);
        response = getRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    /**
     * Generic GET request with path parameters
     */
    protected Response getWithPath(String endpoint, Object... pathParams) {
        logger.info("GET {} with params: {}", endpoint, pathParams);
        response = getRequestSpec()
                .when()
                .get(endpoint, pathParams)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    /**
     * Generic GET request with query parameters
     */
    protected Response getWithQueryParams(String endpoint, Map<String, Object> queryParams) {
        logger.info("GET {} with query params: {}", endpoint, queryParams);
        response = getRequestSpec()
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    // ===== RESPONSE HELPERS =====

    /**
     * Get last response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Get response time in milliseconds
     */
    public long getResponseTime() {
        return response != null ? response.getTime() : 0;
    }

    /**
     * Get response time in specified time unit
     */
    public long getResponseTime(TimeUnit timeUnit) {
        return response != null ? response.getTimeIn(timeUnit) : 0;
    }

    /**
     * Get status code
     */
    public int getStatusCode() {
        return response != null ? response.getStatusCode() : 0;
    }

    // ===== CHAINING HELPERS =====

    /**
     * Store value for chaining
     */
    public void storeValue(String key, Object value) {
        chainedData.put(key, value);
        logger.info("Stored value: {} = {}", key, value);
    }

    /**
     * Get stored value
     */
    public Object getStoredValue(String key) {
        return chainedData.get(key);
    }

    /**
     * Check if value exists
     */
    public boolean hasStoredValue(String key) {
        return chainedData.containsKey(key);
    }

    /**
     * Clear all stored values
     */
    public void clearStoredValues() {
        chainedData.clear();
    }

    /**
     * Get all stored values
     */
    public Map<String, Object> getAllStoredValues() {
        return new HashMap<>(chainedData);
    }
}
