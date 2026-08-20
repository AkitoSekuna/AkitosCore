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

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("akitoscore.admin")) {
                sender.sendMessage(colorize(Main.getLangManager().get("core.no-permission")));
                return true;
            }
            Main.getConfigManager().reload();
            Main.getLangManager().reload();
            Main.notifyReload(ReloadReason.ADMIN_COMMAND);
            sender.sendMessage(colorize(Main.getLangManager().get("core.reload-success")));
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sendInfo(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("addons")) {
            Map<String, String> addons = Main.getRegisteredAddons();
            if (addons.isEmpty()) {
                sender.sendMessage(colorize(Main.getLangManager().get("addons.none")));
                return true;
            }
            sender.sendMessage(colorize(Main.getLangManager().get("addons.header")));
            addons.forEach((name, version) ->
                    sender.sendMessage(colorize(Main.getLangManager().get("addons.entry",
                            Map.of("name", name, "version", version)))));
            return true;
        }

        sendInfo(sender);
        return true;
    }

    private void sendInfo(CommandSender sender) {
        String version = Main.getInstance().getDescription().getVersion();
        sender.sendMessage(colorize(Main.getLangManager().get("info.header")));
        sender.sendMessage(colorize(Main.getLangManager().get("info.version", "version", version)));
        sender.sendMessage(colorize(Main.getLangManager().get("info.author")));
        sender.sendMessage(colorize(Main.getLangManager().get("info.currency",
                "currency", Main.getConfigManager().getCurrencyName())));
        sender.sendMessage(colorize(Main.getLangManager().get("info.language",
                "language", Main.getConfigManager().getLanguage())));
        sender.sendMessage(colorize(Main.getLangManager().get("info.addons",
                "count", String.valueOf(Main.getRegisteredAddons().size()))));
        sender.sendMessage(colorize(Main.getLangManager().get("info.help-reload")));
        sender.sendMessage(colorize(Main.getLangManager().get("info.help-addons")));
    }

    private String colorize(String input) {
        return input.replace("&", "\u00a7");
    }
}
