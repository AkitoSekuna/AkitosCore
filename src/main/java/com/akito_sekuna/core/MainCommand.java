package com.akito_sekuna.core;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
                Main.getCommandIssueTracker().record("no-permission");
                sendLang(sender, "core.no-permission");
                return true;
            }
            Main.getConfigManager().reload();
            Main.getLangManager().reload();
            Main.notifyReload(ReloadReason.ADMIN_COMMAND);
            sendLang(sender, "core.reload-success");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sendInfo(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("addons")) {
            Map<String, String> addons = Main.getRegisteredAddons();
            if (addons.isEmpty()) {
                sendLang(sender, "addons.none");
                return true;
            }
            sendLang(sender, "addons.header");
            addons.forEach((name, version) ->
                    sendLang(sender, "addons.entry", Map.of("name", name, "version", version)));
            return true;
        }

        Main.getCommandIssueTracker().record("unknown-subcommand");
        sendInfo(sender);
        return true;
    }

    private void sendInfo(CommandSender sender) {
        String version = Main.getInstance().getPluginMeta().getVersion();
        sendLang(sender, "info.header");
        sendLang(sender, "info.version", "version", version);
        sendLang(sender, "info.author");
        sendLang(sender, "info.currency", "currency", Main.getConfigManager().getCurrencyName());
        sendLang(sender, "info.language", "language", Main.getConfigManager().getLanguage());
        sendLang(sender, "info.addons", "count", String.valueOf(Main.getRegisteredAddons().size()));
        sendLang(sender, "info.help-reload");
        sendLang(sender, "info.help-addons");
    }

    // --- Lang lookup + Adventure conversion ---
    // ILangAPI/LangManager keep returning raw '&'-coded legacy strings unchanged;
    // this is the single point where that raw text gets turned into a real Component.

    private void sendLang(CommandSender sender, String key) {
        sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Main.getLangManager().get(key)));
    }

    private void sendLang(CommandSender sender, String key, String placeholder, String value) {
        sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Main.getLangManager().get(key, placeholder, value)));
    }

    private void sendLang(CommandSender sender, String key, Map<String, String> replacements) {
        sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Main.getLangManager().get(key, replacements)));
    }
}
