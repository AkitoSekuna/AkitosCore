# AkitosLobby

Lobby teleportation and interactive treasure head addon for the Akitos plugin network. Provides seamless `/lobby` teleportation commands and clickable treasure head rewards with dynamic payouts, long-term cooldowns, and Headsmith skin integration.

## Requirements

* Paper 1.21.1+
* Java 21+
* AkitosCore v21.2.0+
* Headsmith (soft dependency; required only for custom mini-block head skins)

## Installation

1. Install AkitosCore first.
2. (Optional) Install Headsmith if you want custom mini-block textures for treasure heads.
3. Drop `AkitosLobby-21.2.1.jar` into your `plugins/` folder.
4. Restart the server. Default configuration files are generated on first run.

[NOTE: AkitosLobby strictly requires AkitosCore. On startup, it verifies that both plugins share the same Major and Minor version numbers (21.2.X). If a version mismatch is detected, AkitosLobby will automatically disable itself to prevent errors.]

## Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/lobby` | Teleport to the main lobby spawn location | `akitoslobby.use` |
| `/treasurehead [rarity]` | Place a clickable treasure head at your feet | `akitoslobby.admin` |

Aliases: `/hub` (for `/lobby`)

## Permissions

| Permission | Description | Default |
| --- | --- | --- |
| `akitoslobby.use` | Access to `/lobby` and `/hub` commands | true |
| `akitoslobby.admin` | Access to place treasure heads via `/treasurehead` | op |

## Configuration

`AkitosPlugins/AkitosLobby/config.yml`:

| Key | Type | Default | Description |
| --- | --- | --- | --- |
| `lobby-world` | string | `world` | Target world where players are teleported upon executing `/lobby` |
| `treasure-head.cooldown-days` | integer | `30` | Per-player cooldown duration (in days) between claims |
| `treasure-head.rarities.normal.min-reward` | integer | `100` | Minimum coin payout for normal treasure heads |
| `treasure-head.rarities.normal.max-reward` | integer | `500` | Maximum coin payout for normal treasure heads |
| `treasure-head.rarities.normal.headsmith-name` | string | `mini copper block` | Headsmith item identifier used when placing normal heads |
| `treasure-head.rarities.big.min-reward` | integer | `500` | Minimum coin payout for big treasure heads |
| `treasure-head.rarities.big.max-reward` | integer | `2500` | Maximum coin payout for big treasure heads |
| `treasure-head.rarities.big.headsmith-name` | string | `mini gold block` | Headsmith item identifier used when placing big heads |
| `treasure-head.rarities.mega.min-reward` | integer | `2500` | Minimum coin payout for mega treasure heads |
| `treasure-head.rarities.mega.max-reward` | integer | `10000` | Maximum coin payout for mega treasure heads |
| `treasure-head.rarities.mega.headsmith-name` | string | `mini emerald block` | Headsmith item identifier used when placing mega heads |

## Storage

Data is stored persistently in block and player NBT data via Paper's Persistent Data Container (PDC). No external database or additional files are required.

| Container | Key | Type | Contents |
| --- | --- | --- | --- |
| Block PDC (TileEntity) | `treasure_head` | byte | Identifies player head blocks as active treasure heads |
| Block PDC (TileEntity) | `treasure_rarity` | string | Stores the head rarity tier (`normal`, `big`, or `mega`) |
| Player PDC | `treasure_cooldown` | long | Timestamp (ms) of the player's last claimed treasure head |

## Part of the Akitos Plugin Network

AkitosLobby is an official addon designed for the Akitos plugin ecosystem. All plugins share the same network version (`Y` in `vX.Y.Z`) and must be kept in sync.

## Checkout my other plugins

* [AkitosCore](https://github.com/AkitoSekuna/AkitosCore)
* [AkitosDrugs](https://github.com/AkitoSekuna/AkitosDrugs)
* [AkitosGambling](https://github.com/AkitoSekuna/AkitosGambling)
* [AkitosVault](https://github.com/AkitoSekuna/AkitosVault)
