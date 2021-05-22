package com.dfsek.terra.forge.listener;

import com.dfsek.terra.forge.ForgeUtil;
import com.dfsek.terra.forge.TerraForgePlugin;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryListener {
    private static final TerraForgePlugin INSTANCE = TerraForgePlugin.getInstance();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Biome> event) {
        INSTANCE.logger().info("Registering biomes...");
    }

    @SubscribeEvent
    public static void registerPop(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(TerraForgePlugin.POPULATOR_FEATURE);
    }
}
