package com.indodax.runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Test Runner for Indodax API Automation
 * Run with: mvn test
 * Run specific tag: mvn test -Dcucumber.filter.tags="@smoke"
 * Generate Allure report: mvn allure:serve
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.indodax.steps")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@api")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
public class TestRunner {
}
