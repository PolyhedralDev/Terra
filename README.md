# Terra

Terra is an incredibly powerful free & open-source data-driven, platform-agnostic world generator. It allows you to create a world exactly
to your specifications, with no knowledge of Java required.

## Downloads:

* Paper+ servers (Paper, Tuinity, Purpur, etc): [SpigotMC](https://www.spigotmc.org/resources/85151/)
* Fabric: [Modrinth](https://modrinth.com/mod/terra) / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/terra-world-generator)
* Forge **(ALPHA - NOT PRODUCTION-READY)**: [Modrinth](https://modrinth.com/mod/terra) / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/terra-world-generator)

## Building and Running Terra

To build, simply run `./gradlew build` (`gradlew.bat build` on Windows). This will build all platforms, and
produce JARs in `platforms/<platform>/build/libs`

### Production JARs:
* Bukkit: `Terra-<version>-shaded.jar`
* Fabric: `Terra-<version>-shaded-mapped.jar`
* Forge: `Terra-<version>-shaded.jar`

### Building a Specific Platform
To build a specific platform, run `gradlew :platforms:<platform>:build`.

JARs are produced in `platforms/<platform>/build/libs`.

### Running Minecraft in the IDE
To run Minecraft with Terra in the IDE (for testing) use the following tasks:
* Bukkit
  * `installPaper` - Install a [Paper](https://github.com/PaperMC/Paper) test server. (Only needs to be run once).
  * `installPurpur` - Install a [Purpur](https://github.com/pl3xgaming/Purpur) test server. (Only needs to be run once).
  * `runPaper` - Run the Paper test server with Terra (`installPaper` must have been run previously).
  * `runPurpur` - Run the Purpur test server with Terra (`installPurpur` must have been run previously).
* Fabric
  * `runClient` - Run a Minecraft Fabric client with Terra installed.
  * `runServer` - Run a Minecraft Fabric server with Terra installed.
* Forge
  * `runClient` - Run a Minecraft Forge client with Terra installed.
  * `runServer` - Run a Minecraft Forge server with Terra installed.
## Contributing
Contributions are welcome! If you want to see a feature in Terra, please, open an issue, or implement it yourself and
submit a PR!
Join the discord [here](https://discord.gg/PXUEbbF) if you would like to talk more about the project!

## Beta
Terra is still in beta! While it is stable, it is not feature-complete. There is a lot to be added!
