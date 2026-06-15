package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import com.akito_sekuna.core.api.EconomyResult;
import com.akito_sekuna.core.api.IEconomyAPI;
import com.akito_sekuna.core.data.PlayerData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EconomyManager implements IEconomyAPI {

    private static final double MAX_BALANCE = 1_000_000_000.0;
    private static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Main plugin;
    private final File auditLog;

    public EconomyManager(Main plugin) {
        this.plugin = plugin;
        this.auditLog = new File(Main.getPluginFolder(), "economy-audit.log");
    }

    @Override
    public double getBalance(UUID uuid) {
        PlayerData data = plugin.getPlayerDataManager().get(uuid);
        if (data == null) return 0;
        return data.balance();
    }

    @Override
    public EconomyResult setBalance(UUID uuid, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) {
            return EconomyResult.INVALID_AMOUNT;
        }
        if (amount > MAX_BALANCE) {
            return EconomyResult.INVALID_AMOUNT;
        }
        PlayerData data = plugin.getPlayerDataManager().get(uuid);
        if (data == null) return EconomyResult.PLAYER_NOT_FOUND;
        double previous = data.balance();
        plugin.getPlayerDataManager().update(data.withBalance(amount));
        audit(uuid, "SET", previous, amount);
        return EconomyResult.SUCCESS;
    }

    @Override
    public EconomyResult give(UUID uuid, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            return EconomyResult.INVALID_AMOUNT;
        }
        PlayerData data = plugin.getPlayerDataManager().get(uuid);
        if (data == null) return EconomyResult.PLAYER_NOT_FOUND;
        double newBalance = data.balance() + amount;
        if (newBalance > MAX_BALANCE) {
            return EconomyResult.INVALID_AMOUNT;
        }
        plugin.getPlayerDataManager().update(data.withBalance(newBalance));
        audit(uuid, "GIVE", data.balance(), newBalance);
        return EconomyResult.SUCCESS;
    }

    @Override
    public boolean take(UUID uuid, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) return false;
        PlayerData data = plugin.getPlayerDataManager().get(uuid);
        if (data == null) return false;
        if (data.balance() < amount) return false;
        double newBalance = data.balance() - amount;
        plugin.getPlayerDataManager().update(data.withBalance(newBalance));
        audit(uuid, "TAKE", data.balance(), newBalance);
        return true;
    }

    @Override
    public boolean has(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    @Override
    public String format(double amount) {
        return String.format("%.1f %s", amount, plugin.getConfigManager().getCurrencySymbol());
    }

    private void audit(UUID uuid, String operation, double before, double after) {
        String entry = String.format("[%s] %s | %s | before=%.2f after=%.2f",
                LocalDateTime.now().format(LOG_FORMAT), operation, uuid, before, after);
        try (PrintWriter pw = new PrintWriter(new FileWriter(auditLog, true))) {
            pw.println(entry);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to write economy audit log: " + e.getMessage());
        }
    }
}
