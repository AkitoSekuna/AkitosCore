package com.akito_sekuna.core.api;

import com.akito_sekuna.core.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPlayerDataAPI {
    PlayerData get(UUID uuid);
    void update(PlayerData data);
    void save(PlayerData data);
    PlayerData load(Player player);
}