# Releasing (Dev/1.21.10)

This repo uses GitHub Actions to build artifacts on every push and publish release assets on tags.

## What gets built
- Shaded runtime jar: `platforms/bukkit/build/libs/*-shaded.jar` (or `*-all.jar`)
- Sources jar: `platforms/bukkit/build/libs/*-sources.jar`
- Checksums: `*.sha256` for each jar

## Before tagging a release (required checks)

### 1) Build locally (optional but recommended)
From repo root:
- Windows:
  ```powershell
  .\gradlew.bat --stop
  .\gradlew.bat clean :platforms:bukkit:shadowJar :platforms:bukkit:sourcesJar --no-daemon --stacktrace
