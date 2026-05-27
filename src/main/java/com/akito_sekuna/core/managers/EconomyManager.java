package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import com.akito_sekuna.core.api.IEconomyAPI;
import com.akito_sekuna.core.data.PlayerData;

import java.util.UUID;

public class EconomyManager implements IEconomyAPI {

    private final Main plugin;

    public EconomyManager(Main plugin) {
        this.plugin = plugin;
    }

    public double getBalance(UUID uuid) {
        PlayerData data = Main.playerDataManager.get(uuid);
        if (data == null) return 0;
        return data.balance();
    }

    public void setBalance(UUID uuid, double amount) {
        PlayerData data = Main.playerDataManager.get(uuid);
        if (data == null) return;
        Main.playerDataManager.update(data.withBalance(Math.max(0, amount)));
    }

    public void give(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean take(UUID uuid, double amount) {
        double balance = getBalance(uuid);
        if (balance < amount) return false;
        setBalance(uuid, balance - amount);
        return true;
    }

    public boolean has(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    public String format(double amount) {
        return String.format("%.1f %s", amount, Main.configManager.getCurrencySymbol());
    }
}