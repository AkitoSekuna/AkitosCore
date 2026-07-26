package com.akito_sekuna.core;

import com.akito_sekuna.core.api.ICoreAPI;

public interface AkitosAddon {

    String getAddonName();
    String getAddonVersion();

    void onCoreReady(ICoreAPI api);
    void onCoreReload(ICoreAPI newApi, ReloadReason reason);
    void onCoreShutdown();
}
