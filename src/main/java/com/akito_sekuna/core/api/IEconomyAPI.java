package com.akito_sekuna.core.api;

import java.util.UUID;

public interface IEconomyAPI {
    double getBalance(UUID uuid);
    void setBalance(UUID uuid, double amount);
    void give(UUID uuid, double amount);
    boolean take(UUID uuid, double amount);
    boolean has(UUID uuid, double amount);
    String format(double amount);
}