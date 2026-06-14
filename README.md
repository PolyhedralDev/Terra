# NullNomadsWorldgen

NullNomadsWorldgen is a standalone Paper plugin for vanilla-like Overworld
generation. It is derived from a pinned
[Terra](https://github.com/PolyhedralDev/Terra) source snapshot and bundles one
pinned `OVERWORLD` configuration.

This project is not an official Polyhedral Development release and is not
affiliated with or endorsed by Polyhedral Development. The first release
targets only Paper; other Terra platforms are intentionally excluded.

## Building and Running

Clone this repository with submodules, or initialize the pinned Overworld pack
before building:

```shell
git submodule update --init packs/overworld
```

The build packages this local snapshot and does not download config packs from
GitHub. A missing submodule is reported as a build error instead of falling
back to a moving release.

To build, run `./gradlew build` (`gradlew.bat build` on Windows).

### Production JAR

- `platforms/bukkit/build/libs/NullNomadsWorldgen-<version>-shaded.jar`

### Building Paper Only

Run `gradlew :platforms:bukkit:build`.

Use `gradlew :platforms:bukkit:runServer` to run the Paper test server.

## Contributing

Contributions are welcome through the
[NullNomadsWorldgen repository](https://github.com/NullNomads/NullNomadsWorldgen).

## Licensing

Parts of Terra are licensed under either the MIT License or the GNU General
Public License, version 3.0.

* Our API is licensed under the [MIT License](LICENSES/TERRA-MIT.txt), to ensure
  that everyone is able to freely use it however they want.
* Our core addons are also licensed under the
  [MIT License](LICENSES/TERRA-MIT.txt), to ensure that people can freely use
  code from them to learn and make their own addons, without worrying about
  GPL infection.
* Our platform-agnostic implementations and platform implementations are
  licensed under
  the [GNU General Public License, version 3.0](common/implementation/LICENSE),
  to ensure that they remain free software wherever they are used.

The combined NullNomadsWorldgen Paper plugin and new implementation code are
distributed under [GPL-3.0-or-later](LICENSE). See
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) for pinned sources,
attribution, and the applicable license boundaries.

If you're not sure which license a particular file is under, check:

* The file's header
* The LICENSE file in the closest parent folder of the file in question

## Development Status

NullNomadsWorldgen is under active pre-release development and is not ready for
production worlds yet.

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

