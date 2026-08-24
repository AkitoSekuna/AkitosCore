# AkitosCore

Core plugin for the Akitos plugin network. Provides the shared economy, banking, player data, language, and metrics systems every other Akitos plugin builds on. Every other plugin in the network hard-depends on this one; it has no dependencies of its own within the network.

## Requirements

* Paper 1.21.11+
* Java 21+

## Installation

1. Drop `AkitosCore-21.2.5.jar` into your `plugins/` folder.
2. Restart the server. Default configuration and language files are generated on first run.
3. Install any Akitos addon plugin (AkitosGambling, AkitosVault, AkitosDrugs, AkitosLobby) alongside it. Addons will refuse to enable if their own major/minor version (the `X.Y` in `vX.Y.Z`) doesn't match AkitosCore's.

## Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/akitoscore` | Show plugin info (version, currency, language, addon count) | none |
| `/akitoscore reload` | Reload `config.yml` and the active language file, notify all registered addons | `akitoscore.admin` |
| `/akitoscore addons` | List every addon currently registered with Core | none |

Alias: `/ac`

## Permissions

| Permission | Description | Default |
| --- | --- | --- |
| `akitoscore.admin` | Access to `/akitoscore reload` | op |

## Configuration

`AkitosPlugins/AkitosCore/config.yml`:

| Key | Type | Default | Description |
| --- | --- | --- | --- |
| `language` | string | `en` | Active language file. Available: `en`, `ru`. |
| `economy.currency-name` | string | `Pixels` | Display name for the shared currency. |
| `economy.currency-symbol` | string | `px` | Short symbol appended to formatted amounts. |
| `economy.starting-balance` | double | `100.0` | Balance given to a player on their first join. |
| `data.save-interval-seconds` | integer | `300` | How often player data is written to disk, in seconds. Minimum `30`, maximum `3600`. |
| `metrics.enabled` | boolean | `true` | Whether anonymous usage statistics are sent via bStats. Addons report through Core rather than talking to bStats directly, so this one toggle covers the whole network. |

Language files live at `AkitosPlugins/AkitosCore/lang/en.yml` and `lang/ru.yml`. As of `21.2.5` this file is shared across the whole network: alongside Core's own `core`, `info`, and `addons` sections, it holds a labeled, currently-empty block reserved for each addon (`gambling`, `drugs`, `lobby`), so that translating the entire network's player-facing text is a matter of editing one pair of files rather than hunting through every plugin's jar. Addon-specific content in those blocks is planned but not yet populated.

## For addon developers

Get the shared API from anywhere after `AkitosCore` has enabled:

```java
ICoreAPI api = com.akito_sekuna.core.Main.getAPI();
```

`ICoreAPI` exposes:

| Method | Returns | Purpose |
| --- | --- | --- |
| `getEconomy()` | `IEconomyAPI` | Player balances: `getBalance`, `setBalance`, `give`, `take`, `has`, `format`. |
| `getBank()` | `IBankAPI` | Named, account-based banking separate from player balances: `create`, `delete`, `deposit`, `withdraw`, `set`, `getBalance`, `exists`, `format`. |
| `getPlayerData()` | `IPlayerDataAPI` | Read a player's stat record via `get(uuid)`, and targeted mutators (`addKills`, `addDeaths`, `addMobKills`, `addPlaytime`, `addQuestsCompleted`) rather than a full overwrite, so concurrent writers can't clobber each other. |
| `getLang()` | `ILangAPI` | `get(key)` / `get(key, placeholder, value)` / `get(key, replacements)`, returning the raw `&`-coded string from the active language file. Consumers wrap the result in `LegacyComponentSerializer.legacyAmpersand().deserialize(...)` before sending it, Core does not return a `Component` directly. |
| `getMetrics()` | `IMetricsAPI` | Register bStats charts (`registerLineChart`, `registerPieChart`, `registerBarChart`) without talking to the bStats library directly; respects the `metrics.enabled` toggle automatically. |

For lifecycle notifications (config reloads, shutdown), implement `AkitosAddon`:

```java
public interface AkitosAddon {
    String getAddonName();
    String getAddonVersion();
    void onCoreReady(ICoreAPI api);
    void onCoreReload(ICoreAPI newApi, ReloadReason reason);
    void onCoreShutdown();
}
```

Register it in your own `onEnable()`, after Core has already enabled (declare `akitoscore` as a hard `depend` in your `plugin.yml`):

```java
Main.registerAddon(this); // where `this` implements AkitosAddon
```

`onCoreReady` fires synchronously the moment you call `registerAddon`, so it is safe to start using the API immediately afterward in the same method. If you only need to appear in `/akitoscore addons` without any lifecycle callbacks, `Main.registerAddon(String name, String version)` is also available.

## Part of the Akitos Plugin Network

AkitosCore is the mandatory hard dependency for every plugin in the network. All plugins share the same network version (`Y` in `vX.Y.Z`) and must be kept in sync.

* [AkitosGambling](https://github.com/AkitoSekuna/AkitosGambling)
* [AkitosVault](https://github.com/AkitoSekuna/AkitosVault)
* [AkitosDrugs](https://github.com/AkitoSekuna/AkitosDrugs)
* [AkitosLobby](https://github.com/AkitoSekuna/AkitosLobby)
