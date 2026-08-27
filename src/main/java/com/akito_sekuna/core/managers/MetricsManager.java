package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.api.IMetricsAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimpleBarChart;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Owns the network's single bStats instance. Addons never touch org.bstats
 * directly -- they go through IMetricsAPI, and this class is the only thing
 * that talks to bStats itself.
 */
public class MetricsManager implements IMetricsAPI {

    private static final int PLUGIN_ID = 33553;

    private final Metrics metrics;

    public MetricsManager(JavaPlugin plugin, boolean enabled) {
        this.metrics = enabled ? new Metrics(plugin, PLUGIN_ID) : null;
    }

    @Override
    public void registerPieChart(String chartId, Callable<String> valueSupplier) {
        if (metrics != null) {
            metrics.addCustomChart(new SimplePie(chartId, valueSupplier));
        }
    }

    @Override
    public void registerBarChart(String chartId, Callable<Map<String, Integer>> valuesSupplier) {
        if (metrics != null) {
            metrics.addCustomChart(new SimpleBarChart(chartId, valuesSupplier));
        }
    }

    @Override
    public void registerLineChart(String chartId, Callable<Integer> valueSupplier) {
        if (metrics != null) {
            metrics.addCustomChart(new SingleLineChart(chartId, valueSupplier));
        }
    }
}
