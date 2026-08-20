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

    @Override
    public double getBalance(UUID uuid) {
        PlayerData data = Main.getPlayerDataManager().get(uuid);
        if (data == null) return 0;
        return data.balance();
    }

    @Override
    public void setBalance(UUID uuid, double amount) {
        PlayerData data = Main.getPlayerDataManager().get(uuid);
        if (data == null) return;
        Main.getPlayerDataManager().updateData(data.withBalance(Math.max(0, amount)));
    }

    @Override
    public void give(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    @Override
    public boolean take(UUID uuid, double amount) {
        double balance = getBalance(uuid);
        if (balance < amount) return false;
        setBalance(uuid, balance - amount);
        return true;
    }

    @Override
    public boolean has(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    @Override
    public String format(double amount) {
        return String.format("%.1f %s", amount, Main.getConfigManager().getCurrencySymbol());
    }
}
