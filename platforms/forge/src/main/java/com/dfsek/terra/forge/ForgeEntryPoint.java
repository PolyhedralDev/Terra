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

import com.dfsek.terra.forge.AwfulForgeHacks.RegistrySanityCheck;
import com.dfsek.terra.forge.AwfulForgeHacks.RegistryStep;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.forge.data.Codecs;
import com.dfsek.terra.forge.util.LifecycleUtil;

import java.util.concurrent.atomic.AtomicReference;


@Mod("terra")
@EventBusSubscriber(bus = Bus.MOD)
public class ForgeEntryPoint {
    private final RegistrySanityCheck sanityCheck = new RegistrySanityCheck();
    
    static {
        AwfulForgeHacks.loadAllTerraClasses();
        TERRA_PLUGIN = new PlatformImpl();
    }
    
    public static final String MODID = "terra";
    
    private static final Logger logger = LoggerFactory.getLogger(ForgeEntryPoint.class);
    
    private static final PlatformImpl TERRA_PLUGIN;
    
    public static PlatformImpl getPlatform() {
        return TERRA_PLUGIN;
    }
    
    public ForgeEntryPoint() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerBiomes(RegisterEvent event) {
        event.register(Keys.BLOCKS, helper -> sanityCheck.progress(RegistryStep.BLOCK, () -> logger.debug("Block registration detected.")));
        event.register(Keys.BIOMES, helper -> sanityCheck.progress(RegistryStep.BIOME, LifecycleUtil::initialize));
        event.register(Registry.WORLD_PRESET_KEY, helper -> sanityCheck.progress(RegistryStep.WORLD_TYPE, () -> LifecycleUtil.registerWorldTypes(helper)));
        
        
        event.register(Registry.CHUNK_GENERATOR_KEY, helper -> helper.register(new Identifier("terra:terra"), Codecs.FABRIC_CHUNK_GENERATOR_WRAPPER));
        event.register(Registry.BIOME_SOURCE_KEY, helper -> helper.register(new Identifier("terra:terra"), Codecs.TERRA_BIOME_SOURCE));
    }
}
