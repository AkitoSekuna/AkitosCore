package com.akito_sekuna.core;

import com.akito_sekuna.core.api.CoreAPI;
import com.akito_sekuna.core.api.ICoreAPI;
import com.akito_sekuna.core.listeners.PlayerListener;
import com.akito_sekuna.core.managers.ConfigManager;
import com.akito_sekuna.core.managers.EconomyManager;
import com.akito_sekuna.core.managers.LangManager;
import com.akito_sekuna.core.managers.MetricsManager;
import com.akito_sekuna.core.managers.PlayerDataManager;
import com.akito_sekuna.core.managers.SessionTracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    private static Main instance;

    private static ConfigManager configManager;
    private static PlayerDataManager playerDataManager;
    private static EconomyManager economyManager;
    private static LangManager langManager;
    private static SessionTracker sessionTracker;
    private static ICoreAPI api;
    private static MetricsManager metricsManager;

    private static final Map<String, String> registeredAddons = new HashMap<>();
    private static final List<AkitosAddon> lifecycleAddons = new ArrayList<>();

    // --- Plugin folder ---

    public static File getPluginFolder() {
        return new File(instance.getServer().getPluginsFolder(), "AkitosPlugins");
    }

    // --- Addon registry ---

    public static void registerAddon(String name, String version) {
        registeredAddons.put(name, version);
    }

    public static void registerAddon(AkitosAddon addon) {
        registeredAddons.put(addon.getAddonName(), addon.getAddonVersion());
        lifecycleAddons.add(addon);
        addon.onCoreReady(api);
    }

    public static void notifyReload(ReloadReason reason) {
        lifecycleAddons.forEach(addon -> addon.onCoreReload(api, reason));
    }

    public static Map<String, String> getRegisteredAddons() {
        return Collections.unmodifiableMap(registeredAddons);
    }

    // --- Lifecycle ---

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        economyManager = new EconomyManager(this);
        langManager = new LangManager(this);
        sessionTracker = new SessionTracker();
        api = new CoreAPI();
        metricsManager = new MetricsManager(this);
        metricsManager.registerLineChart("registered_addons", () -> getRegisteredAddons().size());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("akitoscore").setExecutor(new MainCommand());
        getCommand("akitoscore").setTabCompleter(new MainTabCompleter());

        int intervalSeconds = configManager.getSaveInterval();
        long intervalTicks = intervalSeconds * 20L;
        Bukkit.getScheduler().runTaskTimer(this, () -> playerDataManager.saveAll(), intervalTicks, intervalTicks);

        getLogger().info("AkitosCore v" + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        lifecycleAddons.forEach(AkitosAddon::onCoreShutdown);
        playerDataManager.saveAll();
        getLogger().info("AkitosCore disabled.");
    }

    // --- Public API entry point ---

    public static ICoreAPI getAPI() {
        return api;
    }

    // --- Internal accessors ---

    public static Main getInstance() {
        return instance;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public static EconomyManager getEconomyManager() {
        return economyManager;
    }

    public static LangManager getLangManager() {
        return langManager;
    }

    public static SessionTracker getSessionTracker() {
        return sessionTracker;
    }

    public static MetricsManager getMetricsManager() {
        return metricsManager;
    }
}
