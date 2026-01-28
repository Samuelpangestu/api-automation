package com.indodax.config;

import java.util.HashMap;
import java.util.Map;

public class ApiConfig {
    private static final Map<String, String> ENVIRONMENTS = new HashMap<>();
    public static final int CONNECTION_TIMEOUT = getIntProperty("api.timeout.connection", 30000);
    public static final int SOCKET_TIMEOUT = getIntProperty("api.timeout.socket", 30000);
    public static final int RESPONSE_TIMEOUT = getIntProperty("api.timeout.response", 30000);

    public static final int MAX_RETRIES = getIntProperty("api.retry.max", 3);
    public static final int RETRY_DELAY = getIntProperty("api.retry.delay", 1000);
    public static final int REQUEST_TIMEOUT_MS = getIntProperty("api.request.timeout", 5000);
    public static final boolean ENABLE_LOGGING = getBooleanProperty("api.logging.enabled", true);
    public static final boolean ENABLE_ALLURE = getBooleanProperty("api.allure.enabled", true);
    public static final String DEFAULT_CONTENT_TYPE = getProperty("api.header.contentType", "application/json");
    public static final String DEFAULT_ACCEPT = getProperty("api.header.accept", "application/json");

    static {
        ENVIRONMENTS.put("production", "https://indodax.com/api");
        ENVIRONMENTS.put("staging", "https://staging.indodax.com/api");
        ENVIRONMENTS.put("development", "https://dev.indodax.com/api");
    }

    public static String getBaseUrl() {
        String env = System.getProperty("env", "production").toLowerCase();
        return ENVIRONMENTS.getOrDefault(env, ENVIRONMENTS.get("production"));
    }

    public static String getCurrentEnvironment() {
        return System.getProperty("env", "production").toLowerCase();
    }

    public static boolean isValidEnvironment(String env) {
        return ENVIRONMENTS.containsKey(env.toLowerCase());
    }

    private static String getProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    private static int getIntProperty(String key, int defaultValue) {
        String value = System.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = System.getProperty(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
}
