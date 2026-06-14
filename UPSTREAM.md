# Upstream Sources

NullNomadsWorldgen is a derivative of Terra with a pinned Overworld
configuration. The commits below are the fixed starting points for this fork.

## Terra

- Repository: https://github.com/PolyhedralDev/Terra
- Branch at the starting point: `master`
- Commit: `25d510156de02cb77f8fb6ffde276417eb19aea4`
- Commit date: 2026-04-28
- Commit URL: https://github.com/PolyhedralDev/Terra/commit/25d510156de02cb77f8fb6ffde276417eb19aea4
- Declared project version: `7.0.0` pre-release

The Terra commit is the source snapshot from which this repository was
forked. The `upstream` Git remote should point to the repository above.

## TerraOverworldConfig

- Repository: https://github.com/PolyhedralDev/TerraOverworldConfig
- Branch at the starting point: `master`
- Commit: `7d5a5c8d1eea9dee83077e3746efda6c74e56888`
- Commit date: 2026-01-02
- Commit URL: https://github.com/PolyhedralDev/TerraOverworldConfig/commit/7d5a5c8d1eea9dee83077e3746efda6c74e56888
- Pack ID: `OVERWORLD`
- Pack version: `2.0.0`

This commit is the only approved starting point for the bundled Overworld
pack. It is vendored as the `packs/overworld` Git submodule with a detached
HEAD at the commit above. License attribution is documented separately.

## Update Policy

Changing either pinned commit requires a dedicated change that documents the
reason, reviews upstream changes and licenses, and records relevant build and
generation test results. Builds must not replace these snapshots with a
moving branch, tag, or `latest` release.
