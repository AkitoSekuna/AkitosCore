package com.akito_sekuna.core.api;

public interface ICoreAPI {
    IEconomyAPI getEconomy();
    IBankAPI getBank();
    IPlayerDataAPI getPlayerData();
    ILangAPI getLang();
    IMetricsAPI getMetrics();
}
