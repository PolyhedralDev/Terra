# Changelog

All notable changes to NullNomadsWorldgen will be documented in this file.

The project follows [Semantic Versioning](https://semver.org/). Release
versions use the `MAJOR.MINOR.PATCH` format.

## Versioning Policy

- `MAJOR` changes may break the public Java API, configuration schemas, pack
  migration contract, or deterministic generation compatibility for existing
  worlds.
- `MINOR` changes add backward-compatible functionality. Opt-in generation
  features must remain disabled unless explicitly configured.
- `PATCH` changes are backward-compatible fixes and must not intentionally
  change generated blocks or biome IDs for identical inputs.

Before `1.0.0`, incompatible changes are allowed but must be called out in the
changelog and migration notes. Every release that changes generation output
must identify the affected inputs and state whether a new world is required.

## [Unreleased]

### Added

- Pinned Terra and TerraOverworldConfig upstream sources.
- Project licensing and third-party attribution.
- Reproducible local bundling of the pinned `OVERWORLD` pack.
