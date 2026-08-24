# AkitosCore Changelog

## 21.2.5

* Restored bank support: `BankManager` is instantiated on enable and exposed via a new `ICoreAPI.getBank()` method. This had been silently disconnected from Core's runtime for several builds (the manager itself was untouched, it was simply never wired up), breaking any addon that called `getBank()`.
* Modernized all player-facing messages to Adventure `Component`s. `MainCommand`'s old raw-string `colorize()` helper is gone; messages now route through the language system and get converted from the `&`-coded strings `ILangAPI` returns via `LegacyComponentSerializer.legacyAmpersand()`.
* Replaced remaining `getDescription().getVersion()` calls with `getPluginMeta().getVersion()`.
* Bumped the `paper-api` dependency to `1.21.11-R0.1-SNAPSHOT`, matching every other plugin in the network.
* `lang.yml` (both `en` and `ru`) restructured into a shared, network-wide translation file: Core's own strings now live in a labeled block alongside empty placeholder blocks reserved for each addon's own text, so future translation work covers the whole network from one file instead of five.
* Fixed a version mismatch between `pom.xml` and `plugin.yml` that would have shipped a jar reporting the wrong version number at runtime.
* Silenced a benign but recurring build warning (bStats' `META-INF/MANIFEST.MF` overlapping Core's own during shading).
* Switched from separate `maven.compiler.source`/`target` properties to `maven.compiler.release`, so the build now validates against the real Java 21 API surface instead of only setting the output bytecode version.

## Earlier versions

Not yet documented. This changelog starts at `21.2.5`, the first version with a full write-up.
