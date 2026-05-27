package com.akito_sekuna.core;

import com.akito_sekuna.core.api.CoreAPI;
import com.akito_sekuna.core.listeners.PlayerListener;
import com.akito_sekuna.core.managers.ConfigManager;
import com.akito_sekuna.core.managers.EconomyManager;
import com.akito_sekuna.core.managers.LangManager;
import com.akito_sekuna.core.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {

    private static Main instance;
    public static ConfigManager configManager;
    public static PlayerDataManager playerDataManager;
    public static EconomyManager economyManager;
    public static LangManager langManager;
    public static CoreAPI api;

    private static final Map<String, String> registeredAddons = new HashMap<>();

    public static File getPluginFolder() {
        return new File(instance.getServer().getPluginsFolder(), "AkitosPlugins");
    }

    public static void registerAddon(String name, String version) {
        registeredAddons.put(name, version);
    }

    public static Map<String, String> getRegisteredAddons() {
        return registeredAddons;
    }

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        economyManager = new EconomyManager(this);
        langManager = new LangManager(this);
        api = new CoreAPI();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("akitoscore").setExecutor(new MainCommand());
        getCommand("akitoscore").setTabCompleter(new MainTabCompleter());

        long saveInterval = configManager.getSaveInterval() * 20L;
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            playerDataManager.saveAll();
        }, saveInterval, saveInterval);

        getLogger().info("AkitosCore enabled!");
    }

    public static CoreAPI getAPI() {
        return api;
    }

    @Override
    public void onDisable() {
        playerDataManager.saveAll();
        getLogger().info("AkitosCore disabled!");
    }

    public static Main getInstance() {
        return instance;
    }
}