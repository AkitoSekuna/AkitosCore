package com.akito_sekuna.core;

import com.akito_sekuna.core.api.CoreAPI;
import com.akito_sekuna.core.listeners.PlayerListener;
import com.akito_sekuna.core.managers.BankManager;
import com.akito_sekuna.core.managers.ConfigManager;
import com.akito_sekuna.core.managers.EconomyManager;
import com.akito_sekuna.core.managers.LangManager;
import com.akito_sekuna.core.managers.PlayerDataManager;
import com.akito_sekuna.core.managers.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main extends JavaPlugin {

    private static Main instance;
    private static CoreAPI api;

    private static final Map<String, AkitosAddon> registeredAddons = new LinkedHashMap<>();

    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private EconomyManager economyManager;
    private LangManager langManager;
    private ServiceRegistry serviceRegistry;
    private BankManager bankManager;

    public static File getPluginFolder() {
        return new File(instance.getServer().getPluginsFolder(), "AkitosPlugins");
    }

    public static void registerAddon(AkitosAddon addon) {
        registeredAddons.put(addon.getAddonName(), addon);
    }

    public static Map<String, AkitosAddon> getRegisteredAddons() {
        return Collections.unmodifiableMap(registeredAddons);
    }

    public static CoreAPI getAPI() {
        return api;
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public BankManager getBankManager() {
        return bankManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        economyManager = new EconomyManager(this);
        langManager = new LangManager(this);
        serviceRegistry = new ServiceRegistry();
        bankManager = new BankManager(this);

        api = new CoreAPI(economyManager, playerDataManager, langManager, serviceRegistry, bankManager);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        PluginCommand command = getCommand("akitoscore");
        if (command != null) {
            command.setExecutor(new MainCommand(this));
            command.setTabCompleter(new MainTabCompleter());
        } else {
            getLogger().severe("Failed to register 'akitoscore' command -- check plugin.yml!");
        }

        long saveInterval = configManager.getSaveInterval() * 20L;
        Bukkit.getScheduler().runTaskTimer(this, () -> playerDataManager.saveAll(), saveInterval, saveInterval);

        registeredAddons.values().forEach(addon -> addon.onCoreReady(api));

        getLogger().info("AkitosCore v" + getPluginMeta().getVersion() + " enabled!");
    }

    public void notifyAddonsReload(ReloadReason reason) {
        registeredAddons.values().forEach(addon -> addon.onCoreReload(api, reason));
    }

    @Override
    public void onDisable() {
        registeredAddons.values().forEach(AkitosAddon::onCoreShutdown);
        serviceRegistry.clear();
        playerDataManager.saveAll();
        getLogger().info("AkitosCore disabled!");
    }
}
