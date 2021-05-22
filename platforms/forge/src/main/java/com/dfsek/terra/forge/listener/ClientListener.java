package com.dfsek.terra.forge.listener;

import com.dfsek.terra.forge.TerraForgePlugin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {
    private static final TerraForgePlugin INSTANCE = TerraForgePlugin.getInstance();

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        INSTANCE.logger().info("Client setup...");
    }
}
