package com.akito_sekuna.core.api;

public interface IBankAPI {
    BankResult create(String name);
    BankResult delete(String name);
    boolean exists(String name);
    double getBalance(String name);
    BankResult deposit(String name, double amount);
    BankResult withdraw(String name, double amount);
    BankResult set(String name, double amount);
    String format(double amount);
}
