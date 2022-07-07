/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.forge;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.forge.AwfulForgeHacks.RegistrySanityCheck;
import com.dfsek.terra.forge.AwfulForgeHacks.RegistryStep;
import com.dfsek.terra.forge.util.BiomeUtil;
import com.dfsek.terra.mod.data.Codecs;


@Mod("terra")
@EventBusSubscriber(bus = Bus.MOD)
public class ForgeEntryPoint {
    public static final String MODID = "terra";
    private static final Logger logger = LoggerFactory.getLogger(ForgeEntryPoint.class);
    private static final ForgePlatform TERRA_PLUGIN;
    static {
        AwfulForgeHacks.loadAllTerraClasses();
        TERRA_PLUGIN = new ForgePlatform();
    }
    private final RegistrySanityCheck sanityCheck = new RegistrySanityCheck();
    
    public ForgeEntryPoint() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
    }
    
    public static ForgePlatform getPlatform() {
        return TERRA_PLUGIN;
    }
    
    public static void initialize(RegisterHelper<Biome> helper) {
        getPlatform().getEventManager().callEvent(
                new PlatformInitializationEvent());
        BiomeUtil.registerBiomes(helper);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerBiomes(RegisterEvent event) {
        event.register(Keys.BLOCKS, helper -> sanityCheck.progress(RegistryStep.BLOCK, () -> logger.debug("Block registration detected.")));
        event.register(Keys.BIOMES, helper -> sanityCheck.progress(RegistryStep.BIOME, () -> initialize(helper)));
        event.register(Registry.WORLD_PRESET_KEY,
                       helper -> sanityCheck.progress(RegistryStep.WORLD_TYPE, () -> TERRA_PLUGIN.registerWorldTypes(helper::register)));
        
        
        event.register(Registry.CHUNK_GENERATOR_KEY,
                       helper -> helper.register(new Identifier("terra:terra"), Codecs.MINECRAFT_CHUNK_GENERATOR_WRAPPER));
        event.register(Registry.BIOME_SOURCE_KEY, helper -> helper.register(new Identifier("terra:terra"), Codecs.TERRA_BIOME_SOURCE));
    }
}
