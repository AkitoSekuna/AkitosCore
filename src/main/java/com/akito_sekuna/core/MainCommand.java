package com.akito_sekuna.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendInfo(sender);
            return true;
        }

        // /ac reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("akitoscore.admin")) {
                sender.sendMessage("§cYou don't have permission to do this!");
                return true;
            }
            Main.configManager.reload();
            Main.langManager.reload();
            sender.sendMessage("§aAkitosCore reloaded!");
            return true;
        }

        // /ac info
        if (args[0].equalsIgnoreCase("info")) {
            sendInfo(sender);
            return true;
        }

        // /ac addons
        if (args[0].equalsIgnoreCase("addons")) {
            Map<String, String> addons = Main.getRegisteredAddons();
            if (addons.isEmpty()) {
                sender.sendMessage("§7No addons registered.");
                return true;
            }
            sender.sendMessage("§8--- §bAkitos Addons §8---");
            addons.forEach((name, version) ->
                    sender.sendMessage("§7" + name + " §8- §fv" + version));
            return true;
        }

        sendInfo(sender);
        return true;
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage("§8--- §bAkitosCore §8---");
        sender.sendMessage("§7Version: §f1.0.0");
        sender.sendMessage("§7Author: §fAkito_Sekuna");
        sender.sendMessage("§7Currency: §f" + Main.configManager.getCurrencyName());
        sender.sendMessage("§7Language: §f" + Main.configManager.getLanguage());
        sender.sendMessage("§7Addons: §f" + Main.getRegisteredAddons().size() + " registered");
        sender.sendMessage("§7/ac reload §8- §7Reload config and lang");
        sender.sendMessage("§7/ac addons §8- §7List registered addons");
    }
}