package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import com.akito_sekuna.core.api.IPlayerDataAPI;
import com.akito_sekuna.core.data.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager implements IPlayerDataAPI {

    private final Main plugin;
    private final File dataFolder;
    private final Map<UUID, PlayerData> cache = new HashMap<>();

    public PlayerDataManager(Main plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(Main.getPluginFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public PlayerData load(Player player) {
        UUID uuid = player.getUniqueId();
        if (cache.containsKey(uuid)) return cache.get(uuid);

        File file = new File(dataFolder, uuid + ".yml");
        if (!file.exists()) {
            PlayerData fresh = PlayerData.createNew(uuid, player.getName(),
                    Main.configManager.getStartingBalance());
            cache.put(uuid, fresh);
            save(fresh);
            return fresh;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        PlayerData data = new PlayerData(
                uuid,
                config.getString("name", player.getName()),
                config.getDouble("balance", Main.configManager.getStartingBalance()),
                config.getInt("kills", 0),
                config.getInt("deaths", 0),
                config.getInt("mob-kills", 0),
                config.getLong("playtime-seconds", 0L),
                config.getInt("quests-completed", 0)
        );
        cache.put(uuid, data);
        return data;
    }

    public void save(PlayerData data) {
        File file = new File(dataFolder, data.uuid() + ".yml");
        FileConfiguration config = new YamlConfiguration();
        config.set("uuid", data.uuid().toString());
        config.set("name", data.name());
        config.set("balance", data.balance());
        config.set("kills", data.kills());
        config.set("deaths", data.deaths());
        config.set("mob-kills", data.mobKills());
        config.set("playtime-seconds", data.playtimeSeconds());
        config.set("quests-completed", data.questsCompleted());
        try { config.save(file); } catch (IOException e) { e.printStackTrace(); }
        cache.put(data.uuid(), data);
    }

    public void update(PlayerData data) {
        cache.put(data.uuid(), data);
    }

    public PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    public void unload(UUID uuid) {
        PlayerData data = cache.remove(uuid);
        if (data != null) save(data);
    }

    public void saveAll() {
        cache.values().forEach(this::save);
    }
}