package com.akito_sekuna.core.data;

import java.util.UUID;

public record PlayerData(
        UUID uuid,
        String name,
        double balance,
        int kills,
        int deaths,
        int mobKills,
        long playtimeSeconds,
        int questsCompleted
) {
    // Convenience method to create a fresh player with default balance
    public static PlayerData createNew(UUID uuid, String name, double startingBalance) {
        return new PlayerData(uuid, name, startingBalance, 0, 0, 0, 0L, 0);
    }

    // Convenience methods to return modified copies since records are immutable
    public PlayerData withBalance(double newBalance) {
        return new PlayerData(uuid, name, newBalance, kills, deaths, mobKills, playtimeSeconds, questsCompleted);
    }

    public PlayerData withKills(int newKills) {
        return new PlayerData(uuid, name, balance, newKills, deaths, mobKills, playtimeSeconds, questsCompleted);
    }

    public PlayerData withDeaths(int newDeaths) {
        return new PlayerData(uuid, name, balance, kills, newDeaths, mobKills, playtimeSeconds, questsCompleted);
    }

    public PlayerData withMobKills(int newMobKills) {
        return new PlayerData(uuid, name, balance, kills, deaths, newMobKills, playtimeSeconds, questsCompleted);
    }

    public PlayerData withPlaytime(long newPlaytime) {
        return new PlayerData(uuid, name, balance, kills, deaths, mobKills, newPlaytime, questsCompleted);
    }

    public PlayerData withQuestsCompleted(int newQuests) {
        return new PlayerData(uuid, name, balance, kills, deaths, mobKills, playtimeSeconds, newQuests);
    }
}