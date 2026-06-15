package com.akito_sekuna.core.api;

import com.akito_sekuna.core.managers.EconomyManager;
import com.akito_sekuna.core.managers.LangManager;
import com.akito_sekuna.core.managers.PlayerDataManager;
import com.akito_sekuna.core.managers.ServiceRegistry;

public class CoreAPI implements ICoreAPI {

    private final EconomyManager economyManager;
    private final PlayerDataManager playerDataManager;
    private final LangManager langManager;
    private final ServiceRegistry serviceRegistry;

    public CoreAPI(EconomyManager economyManager, PlayerDataManager playerDataManager,
                   LangManager langManager, ServiceRegistry serviceRegistry) {
        this.economyManager = economyManager;
        this.playerDataManager = playerDataManager;
        this.langManager = langManager;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public IEconomyAPI getEconomy() {
        return economyManager;
    }

    @Override
    public IPlayerDataAPI getPlayerData() {
        return playerDataManager;
    }

    @Override
    public ILangAPI getLang() {
        return langManager;
    }

    @Override
    public IServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
}
