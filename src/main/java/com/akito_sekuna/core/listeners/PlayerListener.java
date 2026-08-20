package com.akito_sekuna.core.listeners;

import com.akito_sekuna.core.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.getPlayerDataManager().load(event.getPlayer());
        Main.getSessionTracker().startSession(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        long sessionSeconds = Main.getSessionTracker().getSessionSeconds(event.getPlayer().getUniqueId());
        if (sessionSeconds > 0) {
            Main.getPlayerDataManager().addPlaytime(event.getPlayer().getUniqueId(), sessionSeconds);
        }
        Main.getSessionTracker().endSession(event.getPlayer().getUniqueId());
        Main.getPlayerDataManager().unload(event.getPlayer().getUniqueId());
    }
}
