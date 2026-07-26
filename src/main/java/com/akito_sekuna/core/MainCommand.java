package com.akito_sekuna.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MainCommand implements CommandExecutor {

    private final Main plugin;

    public MainCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendInfo(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("akitoscore.admin")) {
                sender.sendMessage("§cYou don't have permission to do this!");
                return true;
            }
            plugin.getConfigManager().reload();
            plugin.getLangManager().reload();
            plugin.notifyAddonsReload(ReloadReason.ADMIN_COMMAND);
            sender.sendMessage("§aAkitosCore reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sendInfo(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("addons")) {
            Map<String, AkitosAddon> addons = Main.getRegisteredAddons();
            if (addons.isEmpty()) {
                sender.sendMessage("§7No addons registered.");
                return true;
            }
            sender.sendMessage("§8--- §bAkitos Addons §8---");
            addons.forEach((name, addon) ->
                    sender.sendMessage("§7" + name + " §8- §f" + addon.getAddonVersion()));
            return true;
        }

        sendInfo(sender);
        return true;
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage("§8--- §bAkitosCore §8---");
        sender.sendMessage("§7Version: §f" + plugin.getPluginMeta().getVersion());
        sender.sendMessage("§7Author: §fAkito_Sekuna");
        sender.sendMessage("§7Currency: §f" + plugin.getConfigManager().getCurrencyName());
        sender.sendMessage("§7Language: §f" + plugin.getConfigManager().getLanguage());
        sender.sendMessage("§7Addons: §f" + Main.getRegisteredAddons().size() + " registered");
        sender.sendMessage("§7/ac reload §8- §7Reload config and lang");
        sender.sendMessage("§7/ac addons §8- §7List registered addons");
    }
}
