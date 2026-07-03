package com.akito_sekuna.core;

import com.akito_sekuna.core.api.ICoreAPI;

public interface AkitosAddon {

    String getName();
    String getVersion();

    // Called by AC after all managers are ready. Obtain and store the API reference here.
    void onCoreReady(ICoreAPI api);

    // Called by AC before it reloads. Flush any cached state here, then use newApi going forward.
    void onCoreReload(ICoreAPI newApi, ReloadReason reason);

    // Called by AC on shutdown. Release any resources here.
    void onCoreShutdown();
}
