package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.Main;
import com.akito_sekuna.core.api.BankResult;
import com.akito_sekuna.core.api.IBankAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BankManager implements IBankAPI {

    private static final double MAX_BALANCE = 1_000_000_000.0;

    private final Main plugin;
    private final File banksFolder;
    private final Map<String, Double> cache = new HashMap<>();

    public BankManager(Main plugin) {
        this.plugin = plugin;
        this.banksFolder = new File(Main.getPluginFolder(), "banks");
        if (!banksFolder.exists() && !banksFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create banks directory: " + banksFolder.getPath());
        }
        loadAll();
    }

    private void loadAll() {
        File[] files = banksFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;
        for (File file : files) {
            String name = file.getName().replace(".yml", "");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            cache.put(name, config.getDouble("balance", 0.0));
        }
    }

    private void save(String name) {
        File file = new File(banksFolder, name + ".yml");
        FileConfiguration config = new YamlConfiguration();
        config.set("balance", cache.getOrDefault(name, 0.0));
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save bank account '" + name + "': " + e.getMessage());
        }
    }

    @Override
    public BankResult create(String name) {
        if (cache.containsKey(name)) return BankResult.ACCOUNT_ALREADY_EXISTS;
        cache.put(name, 0.0);
        save(name);
        return BankResult.SUCCESS;
    }

    @Override
    public BankResult delete(String name) {
        if (!cache.containsKey(name)) return BankResult.ACCOUNT_NOT_FOUND;
        cache.remove(name);
        File file = new File(banksFolder, name + ".yml");
        if (file.exists()) file.delete();
        return BankResult.SUCCESS;
    }

    @Override
    public boolean exists(String name) {
        return cache.containsKey(name);
    }

    @Override
    public double getBalance(String name) {
        return cache.getOrDefault(name, 0.0);
    }

    @Override
    public BankResult deposit(String name, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) return BankResult.INVALID_AMOUNT;
        if (!cache.containsKey(name)) return BankResult.ACCOUNT_NOT_FOUND;
        double newBalance = cache.get(name) + amount;
        if (newBalance > MAX_BALANCE) return BankResult.INVALID_AMOUNT;
        cache.put(name, newBalance);
        save(name);
        return BankResult.SUCCESS;
    }

    @Override
    public BankResult withdraw(String name, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) return BankResult.INVALID_AMOUNT;
        if (!cache.containsKey(name)) return BankResult.ACCOUNT_NOT_FOUND;
        if (cache.get(name) < amount) return BankResult.INSUFFICIENT_FUNDS;
        cache.put(name, cache.get(name) - amount);
        save(name);
        return BankResult.SUCCESS;
    }

    @Override
    public BankResult set(String name, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) return BankResult.INVALID_AMOUNT;
        if (amount > MAX_BALANCE) return BankResult.INVALID_AMOUNT;
        if (!cache.containsKey(name)) return BankResult.ACCOUNT_NOT_FOUND;
        cache.put(name, amount);
        save(name);
        return BankResult.SUCCESS;
    }

    @Override
    public String format(double amount) {
        return String.format("%.1f %s", amount, plugin.getConfigManager().getCurrencySymbol());
    }
}
