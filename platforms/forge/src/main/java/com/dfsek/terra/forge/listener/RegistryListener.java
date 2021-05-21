package com.dfsek.terra.forge.listener;

import com.dfsek.terra.forge.ForgeUtil;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.generation.TerraLevelType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryListener {
    private static final TerraForgePlugin INSTANCE = TerraForgePlugin.getInstance();

    @SubscribeEvent
    public static void registerLevels(RegistryEvent.Register<ForgeWorldType> event) {
        INSTANCE.logger().info("Registering level types...");
        event.getRegistry().register(TerraLevelType.FORGE_WORLD_TYPE);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Biome> event) {
        INSTANCE.setup(); // Setup now because we need the biomes, and this event happens after blocks n stuff
        INSTANCE.getConfigRegistry().forEach(pack -> pack.getBiomeRegistry().forEach((id, biome) -> event.getRegistry().register(ForgeUtil.createBiome(biome)))); // Register all Terra biomes.
    }

    @SubscribeEvent
    public static void registerPop(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(TerraForgePlugin.POPULATOR_FEATURE);
    }
}
