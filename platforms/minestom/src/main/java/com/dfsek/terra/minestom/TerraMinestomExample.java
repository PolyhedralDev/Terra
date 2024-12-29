package com.dfsek.terra.minestom;

import com.dfsek.terra.minestom.world.TerraMinestomWorldBuilder;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;


public class TerraMinestomExample {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        Instance instance = MinecraftServer
            .getInstanceManager()
            .createInstanceContainer();

        TerraMinestomWorldBuilder.from(instance)
            .packById("DEFMOD")
            .seed(0)
            .attach();

        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                instance.loadChunk(x, z);
            }
        }

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().setRespawnPoint(new Pos(0.0, 100.0, 0.0));
        });

        server.start("localhost", 25565);
    }
}
