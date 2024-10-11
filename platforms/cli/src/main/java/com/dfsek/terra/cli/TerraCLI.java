package com.dfsek.terra.cli;

import net.querz.mca.MCAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.util.vector.Vector2Int;
import com.dfsek.terra.cli.world.CLIWorld;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


//TODO auto pull in version
@Command(name = "TerraCLI", mixinStandardHelpOptions = true, version = "6.6.0",
         description = "Generates a Terra World and saves it in minecraft region format.")
public final class TerraCLI implements Callable<Integer> {
    @Option(names = { "-s", "--size"}, description = "Number of regions to generate.")
    private int size = 2;

    @Option(names = { "-p", "--pack"}, description = "Config pack to use.")
    private String pack = "OVERWORLD";

    @Option(names = { "--seed"}, description = "Seed for world generation.")
    private long seed = 0;

    @Option(names = { "--max-height"}, description = "Maximum height of the world.")
    private int maxHeight = 384;

    @Option(names = { "--min-height"}, description = "Minimum height of the world.")
    private int minHeight = -64;

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        Logger LOGGER = LoggerFactory.getLogger(TerraCLI.class);
        LOGGER.info("Starting Terra CLI...");

        CLIPlatform platform = new CLIPlatform();
        platform.getEventManager().callEvent(new PlatformInitializationEvent());

        ConfigPack generate = platform.getConfigRegistry().getByID(pack).orElseThrow();

        CLIWorld world = new CLIWorld(size, seed, maxHeight, minHeight, generate);

        world.generate();

        world.serialize().parallel().forEach(mcaFile -> {
            Vector2Int pos = mcaFile.getLeft();
            String name = MCAUtil.createNameFromRegionLocation(pos.getX(), pos.getZ());
            LOGGER.info("Writing region ({}, {}) to {}", pos.getX(), pos.getZ(), name);

            try {
                MCAUtil.write(mcaFile.getRight(), name);
            } catch(IOException e) {
                e.printStackTrace();
            }
            LOGGER.info("Wrote region to file.");
        });
        LOGGER.info("Done.");
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new TerraCLI()).execute(args);
        System.exit(exitCode);
    }
}
