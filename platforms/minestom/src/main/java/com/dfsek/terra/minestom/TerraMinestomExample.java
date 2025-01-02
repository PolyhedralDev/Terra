package com.dfsek.terra.minestom;

import com.dfsek.terra.minestom.api.filter.ChunkFilter;
import com.dfsek.terra.minestom.api.filter.EvenChunkFilter;
import com.dfsek.terra.minestom.api.filter.NoFeaturesFilter;
import com.dfsek.terra.minestom.api.filter.NoTerrainFilter;
import com.dfsek.terra.minestom.api.filter.SpecificChunkFilter;
import com.dfsek.terra.minestom.api.filter.XFilter;
import com.dfsek.terra.minestom.api.filter.ZFilter;
import com.dfsek.terra.minestom.world.TerraMinestomWorld;
import com.dfsek.terra.minestom.world.TerraMinestomWorldBuilder;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;


public class TerraMinestomExample {
    private static final Logger logger = LoggerFactory.getLogger(TerraMinestomExample.class);
    private final MinecraftServer server = MinecraftServer.init();
    private Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
    private TerraMinestomWorld world;

    public void createNewInstance() {
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
    }

    public void attachTerra(ChunkFilter filter) {
        world = TerraMinestomWorldBuilder.from(instance)
            .defaultPack()
//            .seed(0)
            .filtered(filter)
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
                    } else if(left % 20 == 0) {
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

    public static void main(String[] args) {
        TerraMinestomExample example = new TerraMinestomExample();
        example.attachTerra(null);
        example.preloadWorldAndMeasure();
        example.addScheduler();
        example.addListeners();
        example.addCommands();
        example.bind();
    }

    public class RegenerateCommand extends Command {
        public RegenerateCommand() {
            super("regenerate");

            ArgumentInteger cx = new ArgumentInteger("cx");
            ArgumentInteger cz = new ArgumentInteger("cz");

            setDefaultExecutor((sender, context) -> regenerate(null));
            addSyntax((sender, context) -> regenerate(new NoFeaturesFilter()), new ArgumentLiteral("noFeatures"));
            addSyntax((sender, context) -> regenerate(new NoTerrainFilter()), new ArgumentLiteral("noTerrain"));
            addSyntax((sender, context) -> regenerate(new EvenChunkFilter()), new ArgumentLiteral("evenChunks"));
            addSyntax((sender, context) -> regenerate(new SpecificChunkFilter(context.get(cx), context.get(cz))), new ArgumentLiteral("chunk"), cx, cz);
            addSyntax((sender, context) -> regenerate(new XFilter(context.get(cx))), new ArgumentLiteral("x"), cx);
            addSyntax((sender, context) -> regenerate(new ZFilter(context.get(cz))), new ArgumentLiteral("z"), cz);
        }

        private void regenerate(@Nullable ChunkFilter filter) {
            if (filter == null) {
                instance.sendMessage(Component.text("Regenerating world without filter "));
            } else {
                instance.sendMessage(Component.text("Regenerating world with filter " + filter.getClass().getSimpleName()));
            }
            createNewInstance();
            attachTerra(filter);
            preloadWorldAndMeasure();
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player ->
                player.setInstance(instance, new Pos(0, 100, 0))
            );
        }
    }
}
