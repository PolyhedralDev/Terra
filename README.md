# Terra
Terra is a data-driven world generator based on [Gaea](https://github.com/PolyhedralDev/Gaea). Find out more on our
[Spigot page](https://www.spigotmc.org/resources/85151/)!

## Building and running Terra
To build, simply run `./gradlew build` on Linux/MacOS, or `gradlew.bat build` on Windows.
This will produce a jar in `build/libs` called `Terra-[CURRENT VERSION].jar`.
You can put this right into your plugins dir, along with the correct Gaea version.

If you would like to test it with a default server config, just run `./gradlew setupServer` or
`./gradlew.bat setupServer` to set up the server, then `./gradlew testWithPaper` or `gradlew.bat testWithPaper` to run
the server. If you want a clean installation of the server, re-run the `setupServer` task.
This will download a default server config from [here](https://github.com/PolyhedralDev/WorldGenTestServer)
and install the server in the `target/server` directory, along with all the needed plugins.
   
**Note: You will need to adjust the `NAME` variable `bukkit.yml` of the test server if you are not using the default
Terra config.**

## Contributing
Contributions are welcome! If you want to see a feature in Terra, please, open an issue, or implement it yourself and
submit a PR!
Join the discord [here](https://discord.gg/PXUEbbF) if you would like to talk more about the project!

## Beta
Terra is still in beta! While it is stable, it is not feature-complete. There is a lot to be added!