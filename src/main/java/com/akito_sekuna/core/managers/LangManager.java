package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import com.akito_sekuna.core.api.ILangAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

public class LangManager implements ILangAPI {

    private FileConfiguration lang;
    private final Main plugin;

    public LangManager(Main plugin) {
        this.plugin = plugin;
        load(Main.configManager.getLanguage());
    }

    public void load(String language) {
        File langFolder = new File(Main.getPluginFolder(), "lang");
        if (!langFolder.exists()) langFolder.mkdirs();

        File file = new File(langFolder, language + ".yml");

        // copy from resources if doesn't exist
        if (!file.exists()) {
            try (InputStream in = plugin.getResource("lang/" + language + ".yml")) {
                if (in != null) {
                    Files.copy(in, file.toPath());
                } else {
                    // fallback to en if language file not found
                    plugin.getLogger().warning("Language file '" + language + ".yml' not found, falling back to en.");
                    try (InputStream en = plugin.getResource("lang/en.yml")) {
                        if (en != null) Files.copy(en, new File(langFolder, "en.yml").toPath());
                    }
                    file = new File(langFolder, "en.yml");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        lang = YamlConfiguration.loadConfiguration(file);

        // fallback layer — load en.yml as base so missing keys still work
        File enFile = new File(langFolder, "en.yml");
        if (!enFile.equals(file) && enFile.exists()) {
            FileConfiguration enLang = YamlConfiguration.loadConfiguration(enFile);
            for (String key : enLang.getKeys(true)) {
                if (!lang.contains(key)) {
                    lang.set(key, enLang.get(key));
                }
            }
        }
    }

    public void reload() {
        load(Main.configManager.getLanguage());
    }

    public String get(String key) {
        String value = lang.getString(key);
        if (value == null) {
            plugin.getLogger().warning("Missing lang key: " + key);
            return "§c[missing: " + key + "]";
        }
        return value;
    }

    public String get(String key, Map<String, String> replacements) {
        String value = get(key);
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            value = value.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return value;
    }

    // Convenience method for single replacement
    public String get(String key, String placeholder, String value) {
        return get(key, Map.of(placeholder, value));
    }
}