package com.dfsek.terra.forge.listener;

import com.dfsek.terra.forge.TerraForgePlugin;

import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryListener {
    @SubscribeEvent
    public static void registerPop(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(TerraForgePlugin.POPULATOR_FEATURE);
    }
}
