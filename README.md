<img align="left" width="64" height="64" src="https://raw.githubusercontent.com/wiki/PolyhedralDev/Terra/images/terra_logo.png" alt="Terra Logo">

# Terra

Terra is a modern world generation modding platform, primarily for Minecraft.
Terra allows complete customization of world generation with an advanced API,
tightly integrated with a powerful configuration system.

Terra consists of several parts:

* A voxel world generation API with emphasis on configuration and extensibility
* Several platform implementations, the layer between the API and the platform
  it's running on.
* An addon loader, which allows addons to interface with the Terra API in a
  platform-agnostic setting
* Several "core addons," which implement the "default" configurations of Terra.
  These addons can be thought of as the config "standard library"

Terra currently officially supports the Fabric mod loader and the Bukkit API
(Paper and friends). We welcome Pull Requests implementing additional platforms!

## Downloads:

* Fabric: [Modrinth](https://modrinth.com/mod/terra)
  / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/terra-world-generator)
* Paper+ servers (Paper, Tuinity, Purpur,
  etc): [SpigotMC](https://www.spigotmc.org/resources/85151/)

## Building and Running Terra

To build, simply run `./gradlew build` (`gradlew.bat build` on Windows). This
will build all platforms, and produce JARs in `platforms/<platform>/build/libs`

### Production JARs:

* Bukkit: `Terra-<version>-shaded.jar`
* Fabric: `Terra-<version>-shaded-mapped.jar`

### Building a Specific Platform

To build a specific platform, run `gradlew :platforms:<platform>:build`.

JARs are produced in `platforms/<platform>/build/libs`.

### Running Minecraft in the IDE

To run Minecraft with Terra in the IDE (for testing) use the following tasks:

* Bukkit
    * `installPaper` - Install a [Paper](https://github.com/PaperMC/Paper) test
      server. (Only needs to be run once).
    * `installPurpur` - Install a [Purpur](https://github.com/pl3xgaming/Purpur)
      test server. (Only needs to be run once).
    * `runPaper` - Run the Paper test server with Terra (`installPaper` must
      have been run previously).
    * `runPurpur` - Run the Purpur test server with Terra (`installPurpur` must
      have been run previously).
* Fabric
    * `runClient` - Run a Minecraft Fabric client with Terra installed.
    * `runServer` - Run a Minecraft Fabric server with Terra installed.

## Contributing

Contributions are welcome! If you want to see a feature in Terra, please, open
an issue, or implement it yourself and submit a PR!
Join the discord [here](https://discord.gg/PXUEbbF) if you would like to talk
more about the project!

## Licensing

Parts of Terra are licensed under either the MIT License or the GNU General
Public License, version 3.0.

* Our API is licensed under the [MIT License](LICENSE), to ensure that everyone
  is able to freely use it however they want.
* Our core addons are also licensed under the [MIT License](LICENSE), to ensure
  that people can freely use code from them to learn and make their own addons,
  without worrying about GPL infection.
* Our platform-agnostic implementations and platform implementations are
  licensed under
  the [GNU General Public License, version 3.0](common/implementation/LICENSE),
  to ensure that they remain free software wherever they are used.

If you're not sure which license a particular file is under, check:

* The file's header
* The LICENSE file in the closest parent folder of the file in question

## Beta

Terra is still in beta! While it is stable, it is not feature-complete. There is
a lot to be added!

## Special Thanks

[![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)](https://www.yourkit.com/)

YourKit has granted Polyhedral Development an open-source license to their
outstanding Java profiler, allowing us to make our software as performant as it
can be!

YourKit supports open source projects with innovative and intelligent tools for
monitoring and profiling Java and .NET applications. YourKit is the creator of
the
[YourKit Java Profiler](https://www.yourkit.com/java/profiler/),
[YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/),
and [YourKit YouMonitor](https://www.yourkit.com/youmonitor/).

