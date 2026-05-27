# AkitosCore

The backbone of the Akito's plugin network. Provides shared economy, player data, and language systems for all Akito plugins.

## Requirements
- Paper 1.21.11+
- Java 21+

## Installation
Drop `AkitosCore.jar` into your `plugins/` folder. All Akito plugins depend on it.

All plugin files are stored in `plugins/AkitosPlugins/`.

## Commands
| Command | Description | Permission |
|---------|-------------|------------|
| `/ac info` | Show plugin info | `akitoscore.admin` |
| `/ac reload` | Reload config and lang | `akitoscore.admin` |
| `/ac addons` | List registered addons | `akitoscore.admin` |

## For Developers
AkitosCore exposes a clean API for other plugins to hook into:

```java
ICoreAPI core = com.akito_sekuna.core.Main.getAPI();
core.getEconomy().give(player.getUniqueId(), 100);
core.getPlayerData().get(player.getUniqueId());
```

Add AkitosCore as a dependency in your `plugin.yml`:
```yaml
depend: [AkitosCore]
```

## Configuration
`config.yml`:
```yaml
language: en
economy:
  currency-name: "Pixels"
  currency-symbol: "px"
  starting-balance: 100.0
data:
  save-interval-seconds: 300
```

## Part of the Akito's Plugin Network
- [AkitosDrugs](https://github.com/AkitoSekuna/AkitosDrugs)
