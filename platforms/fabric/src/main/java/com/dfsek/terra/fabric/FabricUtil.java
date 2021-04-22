package com.dfsek.terra.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class FabricUtil {
    @SuppressWarnings("deprecation")
    public static MinecraftServer getServer() {
        Object game = FabricLoader.getInstance().getGameInstance();
        if(game instanceof MinecraftDedicatedServer) {
            return (MinecraftDedicatedServer) game;
        } else if(game instanceof MinecraftClient) {
            MinecraftClient client = (MinecraftClient) game;
            if(!client.isIntegratedServerRunning()) {
                throw new IllegalStateException("Client is not running integrated server: " + client);
            }
            return client.getServer();
        }
        throw new IllegalStateException("No MinecraftServer initialized: " + game);
    }
}
