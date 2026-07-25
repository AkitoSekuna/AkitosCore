# AkitosCore

The central dependency for the Akitos plugin network. Provides shared economy, bank accounts, player data, language, and addon lifecycle management for all other plugins in the network.

## Requirements

- Paper 1.21.1+
- Java 21+

## Installation

Drop `AkitosCore-21.2.0.jar` into your `plugins/` folder. AkitosCore must load before any other Akitos plugin. No additional configuration is required to get started -- default config files are generated on first run.

If you need EssentialsX or LuckPerms to read player balances, also install `AkitosVault.jar` alongside `Vault.jar`.

## Commands

| Command | Description | Permission |
|---|---|---|
| `/ac info` | Show version, currency, and language info | none |
| `/ac reload` | Reload config and lang, notify all addons | `akitoscore.admin` |
| `/ac addons` | List all registered addons and their versions | `akitoscore.admin` |

Aliases: `/akitoscore`

## Permissions

| Permission | Description | Default |
|---|---|---|
| `akitoscore.admin` | Access to reload and addons subcommands | op |

## Configuration

`AkitosPlugins/AkitosCore/config.yml`:

| Key | Type | Default | Description |
|---|---|---|---|
| `language` | string | `en` | Language file to load from the `lang/` folder |
| `economy.currency-name` | string | `Pixels` | Display name of the currency |
| `economy.currency-symbol` | string | `px` | Short symbol used in menus and messages |
| `economy.starting-balance` | double | `100.0` | Balance given to new players on first join |
| `data.save-interval-seconds` | integer | `300` | How often all online player data is flushed to disk |

## Storage

All data is stored as flat YAML files. No database is required or supported.

| Path | Contents |
|---|---|
| `AkitosPlugins/AkitosCore/config.yml` | Plugin configuration |
| `AkitosPlugins/AkitosCore/lang/en.yml` | Default language file |
| `AkitosPlugins/AkitosCore/playerdata/<uuid>.yml` | One file per player |
| `AkitosPlugins/AkitosCore/banks/<name>.yml` | One file per bank account |
| `AkitosPlugins/AkitosCore/economy-audit.log` | Transaction audit log |

## For Developers

AkitosCore exposes a public API for use by other plugins in the network. All systems are accessed through `ICoreAPI`.

### Getting the API

```java
ICoreAPI api = com.akito_sekuna.core.Main.getAPI();
```

[NOTE: Do not call `getAPI()` in `onEnable`. Obtain the reference inside `onCoreReady` instead -- see the addon lifecycle section below.]

### Addon Registration

Every addon must implement `AkitosAddon` and register itself in `onEnable`:

```java
public class Main extends JavaPlugin implements AkitosAddon {

    private ICoreAPI coreAPI;

    @Override
    public String getAddonName() { return "MyPlugin"; }

    @Override
    public String getAddonVersion() { return getPluginMeta().getVersion(); }

    @Override
    public void onCoreReady(ICoreAPI api) {
        this.coreAPI = api;
    }

    @Override
    public void onCoreReload(ICoreAPI newApi, ReloadReason reason) {
        this.coreAPI = newApi;
    }

    @Override
    public void onCoreShutdown() {}

    @Override
    public void onEnable() {
        com.akito_sekuna.core.Main.registerAddon(this);
    }
}
```

Your `plugin.yml` must declare `AkitosCore` as a hard dependency:

```yaml
depend: [AkitosCore]
```

### Economy API

```java
// Player balances
EconomyResult result = api.getEconomy().give(uuid, 100.0);
boolean taken = api.getEconomy().take(uuid, 50.0);
EconomyResult result = api.getEconomy().setBalance(uuid, 500.0);
double balance = api.getEconomy().getBalance(uuid);
boolean canAfford = api.getEconomy().has(uuid, 25.0);
String formatted = api.getEconomy().format(100.0); // "100.0 px"
```

`give` and `setBalance` return `EconomyResult`. `take` returns `false` if the player has insufficient funds rather than going negative.

`EconomyResult` values: `SUCCESS`, `INSUFFICIENT_FUNDS`, `INVALID_AMOUNT`, `PLAYER_NOT_FOUND`

### Bank API

```java
// Named shared accounts
api.getBank().create("gang_vault");
api.getBank().deposit("gang_vault", 1000.0);
api.getBank().withdraw("gang_vault", 200.0);
api.getBank().set("gang_vault", 0.0);
double balance = api.getBank().getBalance("gang_vault");
boolean exists = api.getBank().exists("gang_vault");
api.getBank().delete("gang_vault");
```

All mutating operations return `BankResult`. `BankResult` values: `SUCCESS`, `INSUFFICIENT_FUNDS`, `INVALID_AMOUNT`, `ACCOUNT_NOT_FOUND`, `ACCOUNT_ALREADY_EXISTS`

### Player Data API

```java
PlayerData data = api.getPlayerData().get(uuid);
```

Player data is loaded on join and unloaded on quit. Do not call `get` for players who are not online.

### Language API

```java
String message = api.getLang().get("some.message.key");
String withPlaceholder = api.getLang().get("some.key", "player", player.getName());
```

Missing keys return `[missing: some.key]` and log a warning.

### Service Registry

The service registry allows addons to expose optional services to the rest of the network without creating hard dependencies between plugins.

```java
// Register a service (do this in onCoreReady)
api.getServiceRegistry().register(MyService.class, myImpl);

// Consume a service -- always returns Optional
Optional<MyService> svc = api.getServiceRegistry().get(MyService.class);
svc.ifPresent(s -> s.doSomething());

// Unregister
api.getServiceRegistry().unregister(MyService.class);
```

Always handle the absent case. Never assume a service is present.

### pom.xml dependency

```xml
<dependency>
    <groupId>com.akito_sekuna</groupId>
    <artifactId>AkitosCore</artifactId>
    <version>21.2.0</version>
    <scope>provided</scope>
</dependency>
```

## Part of the Akitos Plugin Network

AkitosCore is the required base for all other plugins in the network. Feature plugins include AkitosGambling, AkitosDrugs, AkitosVault, and others. All plugins share the same network version (`Y` in `vX.Y.Z`) and must be kept in sync.

## Checkout my other plugins
- [AkitosDrugs](https://github.com/AkitoSekuna/AkitosDrugs)
- [AkitosGambling](https://github.com/AkitoSekuna/AkitosGambling)
- [AkitosVault](https://github.com/AkitoSekuna/AkitosVault)
