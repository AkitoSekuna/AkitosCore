package com.akito_sekuna.core.api;

import com.akito_sekuna.core.data.PlayerData;

import java.util.UUID;

public interface IPlayerDataAPI {

    // Read the current cached data for a player.
    // Returns null if the player is not online.
    PlayerData get(UUID uuid);

    // Targeted mutators -- use these instead of a full update() to avoid overwriting
    // changes made by other systems between get() and write-back.
    void addKills(UUID uuid, int amount);
    void addDeaths(UUID uuid, int amount);
    void addMobKills(UUID uuid, int amount);
    void addPlaytime(UUID uuid, long seconds);
    void addQuestsCompleted(UUID uuid, int amount);
}
