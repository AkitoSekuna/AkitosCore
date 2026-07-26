package com.akito_sekuna.core.api;

import com.akito_sekuna.core.managers.BankManager;
import com.akito_sekuna.core.managers.EconomyManager;
import com.akito_sekuna.core.managers.LangManager;
import com.akito_sekuna.core.managers.PlayerDataManager;
import com.akito_sekuna.core.managers.ServiceRegistry;

public class CoreAPI implements ICoreAPI {

    private final EconomyManager economyManager;
    private final PlayerDataManager playerDataManager;
    private final LangManager langManager;
    private final ServiceRegistry serviceRegistry;
    private final BankManager bankManager;

    public CoreAPI(EconomyManager economyManager, PlayerDataManager playerDataManager,
                   LangManager langManager, ServiceRegistry serviceRegistry,
                   BankManager bankManager) {
        this.economyManager = economyManager;
        this.playerDataManager = playerDataManager;
        this.langManager = langManager;
        this.serviceRegistry = serviceRegistry;
        this.bankManager = bankManager;
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

    @Override
    public IBankAPI getBank() {
        return bankManager;
    }
}
