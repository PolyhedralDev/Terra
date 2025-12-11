package com.dfsek.terra.minestom;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.world.DimensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import com.dfsek.terra.minestom.world.TerraMinestomWorld;


public class TerraMinestomExample {
    private static final Logger logger = LoggerFactory.getLogger(TerraMinestomExample.class);
    private final MinecraftServer server = MinecraftServer.init();
    private final TerraMinestomPlatform platform = new TerraMinestomPlatform();
    private Instance instance;
    private TerraMinestomWorld world;

    public static void main(String[] args) {
        System.setProperty("minestom.registry.unsafe-ops", "true");

        TerraMinestomExample example = new TerraMinestomExample();
        example.createNewInstance();
        example.attachTerra();
        example.preloadWorldAndMeasure();
        example.addScheduler();
        example.addListeners();
        example.addCommands();
        example.bind();
    }

    public void createNewInstance() {
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
    }

    public void attachTerra() {
        world = platform.worldBuilder(instance)
            .packByDefaultMeta(DimensionType.OVERWORLD)
            .attach();
    }

    private void sendProgressBar(int current, int max) {
        String left = "#".repeat((int) ((((float) current) / max) * 20));
        String right = ".".repeat(20 - left.length());
        int percent = (int) (((float) current) / max * 100);
        String percentString = percent + "%";
        percentString = " ".repeat(4 - percentString.length()) + percentString;
        String message = percentString + " |" + left + right + "| " + current + "/" + max;
        logger.info(message);
    }

    public void preloadWorldAndMeasure() {
        int radius = 12;
        int chunksLoading = (radius * 2 + 1) * (radius * 2 + 1);
        AtomicInteger chunksLeft = new AtomicInteger(chunksLoading);

        long start = System.nanoTime();
        for(int x = -radius; x <= radius; x++) {
            for(int z = -radius; z <= radius; z++) {
                instance.loadChunk(x, z).thenAccept(chunk -> {
                    int left = chunksLeft.decrementAndGet();
                    if(left == 0) {
                        long end = System.nanoTime();
                        sendProgressBar(chunksLoading - left, chunksLoading);
                        double chunksPerSecond = chunksLoading / ((end - start) / 1000000000.0);
                        logger.info(
                            "Preloaded {} chunks in world in {}ms. That's {} Chunks/s",
                            chunksLoading,
                            (end - start) / 1000000.0,
                            chunksPerSecond
                        );

                        world.displayStats();
                    } else if(left % 60 == 0) {
                        sendProgressBar(chunksLoading - left, chunksLoading);
                    }
                });
            }
        }
    }

    public void addListeners() {
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setRespawnPoint(new Pos(0.0, 100.0, 0.0));
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        });
    }

    public void addScheduler() {
        MinecraftServer.getSchedulerManager().buildTask(() -> world.displayStats())
            .repeat(Duration.ofSeconds(10))
            .schedule();
    }

    public void addCommands() {
        MinecraftServer.getCommandManager().register(new RegenerateCommand());
    }

    public void bind() {
        logger.info("Starting server on port 25565");
        server.start("localhost", 25565);
    }


    public class RegenerateCommand extends Command {
        public RegenerateCommand() {
            super("regenerate");
            setDefaultExecutor((sender, context) -> regenerate());
        }

        private void regenerate() {
            instance.sendMessage(Component.text("Regenerating world"));
            Instance oldInstance = instance;
            platform.reload();
            createNewInstance();
            attachTerra();
            preloadWorldAndMeasure();
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player ->
                player.setInstance(instance, new Pos(0, 100, 0))
            );

            MinecraftServer.getInstanceManager().unregisterInstance(oldInstance);
        }
    }
}
