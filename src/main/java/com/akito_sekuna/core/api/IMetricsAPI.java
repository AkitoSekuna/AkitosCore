package com.akito_sekuna.core.api;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Lets addons hand over data to be reported through Core's shared bStats instance,
 * without needing to know bStats exists. Core is the only thing that ever talks
 * to bStats directly -- addons just register what they want tracked.
 */
public interface IMetricsAPI {

    /**
     * Registers a pie chart -- one value per report, grouped into slices.
     * Good for a single category/setting, e.g. "which language is configured".
     */
    void registerPieChart(String chartId, Callable<String> valueSupplier);

    /**
     * Registers a bar chart -- a map of category to count.
     * Good for feature adoption across several options at once.
     */
    void registerBarChart(String chartId, Callable<Map<String, Integer>> valuesSupplier);

    /**
     * Registers a single line chart -- one running number over time.
     * Good for things like "active games per server" or "registered addons".
     */
    void registerLineChart(String chartId, Callable<Integer> valueSupplier);
}
