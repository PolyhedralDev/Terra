# Changelog

## v7.0.3 (Dev/1.21.10 tested)
### Java 21–25 support
- Compatibility: server can run on Java 21 through Java 25.
- QoL: suppress/downgrade noisy Seismic reflection message about `sun.misc.Unsafe` missing `theUnsafe` on newer JVMs.
- QoL: startup runtime banner logs detected Java + supported range (21–25) + recommended LTS (21).

### 1.21.10 pack-load stability fixes
- Fix: BlockData parser now tolerates TerraScript-style `{...}` suffix by stripping unsupported trailing data before calling Bukkit BlockData parsing.
  - Example fixed: `minecraft:chest{LootTable:'...'}` no longer crashes pack load on 1.21.10.
- Fix: Entity type parser now tolerates TerraScript-style `{...}` suffix by stripping it before resolving Bukkit `EntityType`.
  - Example fixed: `END_CRYSTAL{SHOWBOTTOM:0}` no longer crashes pack load on 1.21.10.

### Loot generation fix (the original target)
- Fix: dungeon/spawner chest loot now generates by restoring missing loot-table metadata (`minecraft:chests/simple_dungeon`) when Terra generation produced a chest without a loot table.
- Options:
  - generation-time repair
  - retroactive repair (chunk load), optional "only if empty" safety
  - pre-open repair hook for Lootin compatibility

---

## v7.0.2
- Fix: TerraScript entity ids with `{...}` suffix no longer crash Bukkit entity resolution.

## v7.0.1
- Fix: Block strings with `{...}` suffix no longer crash Bukkit BlockData parsing.

## v7.0.0
- Added loot-fix system (dungeon loot table restore + retroactive + Lootin pre-open compatibility).
- Added `/terra lootfix` tooling (status/scan).
- Build reliability improvements for Windows and ZIP builds.
