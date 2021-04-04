package com.dfsek.terra.minestom;


import com.dfsek.terra.minestom.commands.TeleportCommand;
import com.dfsek.terra.minestom.generator.MinestomChunkGeneratorWrapper;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.Position;

public final class MinestomEntry {
    public static void main(String... args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer container = instanceManager.createInstanceContainer();

        TerraMinestomPlugin plugin = new TerraMinestomPlugin();

        DefaultChunkGenerator3D chunkGenerator3D = new DefaultChunkGenerator3D(plugin.getConfigRegistry().get("DEFAULT"), plugin);

        container.setChunkGenerator(new MinestomChunkGeneratorWrapper(chunkGenerator3D, container));

        MinecraftServer.getBiomeManager().unmodifiableCollection().forEach(biome -> System.out.println(biome.getId() + ": " + biome.toNbt()));

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addEventCallback(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(container);
            player.setRespawnPoint(new Position(0, 64, 0));
        });

        MinecraftServer.getCommandManager().register(new TeleportCommand());


        globalEventHandler.addEventCallback(PlayerSpawnEvent.class, event -> event.getPlayer().setGameMode(GameMode.SPECTATOR));

        server.start("0.0.0.0", 25565);
    }
}
