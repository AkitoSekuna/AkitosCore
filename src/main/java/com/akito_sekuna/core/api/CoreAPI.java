package com.akito_sekuna.core.api;

import com.akito_sekuna.core.Main;

public class CoreAPI implements ICoreAPI {

    @Override
    public IEconomyAPI getEconomy() {
        return Main.economyManager;
    }

    @Override
    public IPlayerDataAPI getPlayerData() {
        return Main.playerDataManager;
    }

    @Override
    public ILangAPI getLang() {
        return Main.langManager;
    }
}