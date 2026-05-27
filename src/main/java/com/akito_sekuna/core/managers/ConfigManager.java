package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {

    private FileConfiguration config;
    private final File file;

    public ConfigManager(Main plugin) {
        file = new File(Main.getPluginFolder(), "config.yml");
        if (!file.exists()) {
            Main.getPluginFolder().mkdirs();
            try (InputStream in = plugin.getResource("config.yml")) {
                if (in != null) Files.copy(in, file.toPath());
            } catch (Exception e) { e.printStackTrace(); }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    // --- Language ---

    public String getLanguage() {
        return config.getString("language", "en");
    }

    // --- Economy ---

    public String getCurrencyName() {
        return config.getString("economy.currency-name", "Pixels");
    }

    public String getCurrencySymbol() {
        return config.getString("economy.currency-symbol", "px");
    }

    public double getStartingBalance() {
        return config.getDouble("economy.starting-balance", 100.0);
    }

    // --- Data ---

    public int getSaveInterval() {
        return config.getInt("data.save-interval-seconds", 300);
    }
}