package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import com.akito_sekuna.core.api.IPlayerDataAPI;
import com.akito_sekuna.core.data.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager implements IPlayerDataAPI {

    private final Main plugin;
    private final File dataFolder;
    private final ConcurrentHashMap<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public PlayerDataManager(Main plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(Main.getPluginFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    // --- Internal lifecycle methods (not on public API) ---

    public void load(Player player) {
        UUID uuid = player.getUniqueId();
        if (cache.containsKey(uuid)) return;

        File file = new File(dataFolder, uuid + ".yml");
        if (!file.exists()) {
            PlayerData fresh = PlayerData.createNew(uuid, player.getName(),
                    Main.getConfigManager().getStartingBalance());
            cache.put(uuid, fresh);
            save(fresh);
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        PlayerData data = new PlayerData(
                uuid,
                config.getString("name", player.getName()),
                config.getDouble("balance", Main.getConfigManager().getStartingBalance()),
                config.getInt("kills", 0),
                config.getInt("deaths", 0),
                config.getInt("mob-kills", 0),
                config.getLong("playtime-seconds", 0L),
                config.getInt("quests-completed", 0)
        );
        cache.put(uuid, data);
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
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player data for " + data.uuid() + ": " + e.getMessage());
        }
        cache.put(data.uuid(), data);
    }

    // Internal full-replace update used by EconomyManager.
    public void updateData(PlayerData data) {
        cache.put(data.uuid(), data);
    }

    public void unload(UUID uuid) {
        PlayerData data = cache.remove(uuid);
        if (data != null) save(data);
    }

    public void saveAll() {
        cache.values().forEach(this::save);
    }

    // --- IPlayerDataAPI (public API) ---

    @Override
    public PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public void addKills(UUID uuid, int amount) {
        PlayerData data = cache.get(uuid);
        if (data == null) return;
        cache.put(uuid, data.withKills(data.kills() + amount));
    }

    @Override
    public void addDeaths(UUID uuid, int amount) {
        PlayerData data = cache.get(uuid);
        if (data == null) return;
        cache.put(uuid, data.withDeaths(data.deaths() + amount));
    }

    @Override
    public void addMobKills(UUID uuid, int amount) {
        PlayerData data = cache.get(uuid);
        if (data == null) return;
        cache.put(uuid, data.withMobKills(data.mobKills() + amount));
    }

    @Override
    public void addPlaytime(UUID uuid, long seconds) {
        PlayerData data = cache.get(uuid);
        if (data == null) return;
        cache.put(uuid, data.withPlaytime(data.playtimeSeconds() + seconds));
    }

    @Override
    public void addQuestsCompleted(UUID uuid, int amount) {
        PlayerData data = cache.get(uuid);
        if (data == null) return;
        cache.put(uuid, data.withQuestsCompleted(data.questsCompleted() + amount));
    }
}
