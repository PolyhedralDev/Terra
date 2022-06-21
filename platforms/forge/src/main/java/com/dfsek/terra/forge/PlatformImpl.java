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

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import net.minecraft.MinecraftVersion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.BiomeEffects.GrassColorModifier;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.addon.EphemeralAddon;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.forge.generation.ForgeChunkGeneratorWrapper;
import com.dfsek.terra.forge.handle.ForgeItemHandle;
import com.dfsek.terra.forge.util.ProtoPlatformBiome;
import com.dfsek.terra.mod.handle.MinecraftWorldHandle;


public class PlatformImpl extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformImpl.class);
    private final ItemHandle itemHandle = new ForgeItemHandle();
    private final WorldHandle worldHandle = new MinecraftWorldHandle();
    private final Lazy<File> dataFolder = Lazy.lazy(() -> new File("./config/Terra"));
    
    public PlatformImpl() {
        load();
    }
    
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        getRawConfigRegistry().clear();
        boolean succeed = getRawConfigRegistry().loadAll(this);
        
        MinecraftServer server = getServer();
        
        if(server != null) {
            server.reloadResources(server.getDataPackManager().getNames()).exceptionally(throwable -> {
                LOGGER.warn("Failed to execute reload", throwable);
                return null;
            }).join();
            //BiomeUtil.registerBiomes();
            server.getWorlds().forEach(world -> {
                if(world.getChunkManager().getChunkGenerator() instanceof ForgeChunkGeneratorWrapper chunkGeneratorWrapper) {
                    getConfigRegistry().get(chunkGeneratorWrapper.getPack().getRegistryKey()).ifPresent(pack -> {
                        chunkGeneratorWrapper.setPack(pack);
                        LOGGER.info("Replaced pack in chunk generator for world {}", world);
                    });
                }
            });
        }
        return succeed;
    }
    
    @Override
    protected Iterable<BaseAddon> platformAddon() {
        List<BaseAddon> addons = new ArrayList<>();
        
        addons.add(new ForgeAddon(this));
        
        String mcVersion = MinecraftVersion.CURRENT.getReleaseTarget();
        try {
            addons.add(new EphemeralAddon(Versions.parseVersion(mcVersion), "minecraft"));
        } catch(ParseException e) {
            try {
                addons.add(new EphemeralAddon(Versions.parseVersion(mcVersion + ".0"), "minecraft"));
            } catch(ParseException ex) {
                LOGGER.warn("Failed to parse Minecraft version", e);
            }
        }
        
        FMLLoader.getLoadingModList().getMods().forEach(mod -> {
            String id = mod.getModId();
            if(id.equals("terra") || id.equals("minecraft") || id.equals("java")) return;
            Version version = Versions.getVersion(mod.getVersion().getMajorVersion(), mod.getVersion().getMinorVersion(), mod.getVersion().getIncrementalVersion());
            addons.add(new EphemeralAddon(version, "forge:" + id));
        });
        
        return addons;
    }
    
    @Override
    public @NotNull String platformName() {
        return "Forge";
    }
    
    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public @NotNull File getDataFolder() {
        return dataFolder.value();
    }
    
    @Override
    public @NotNull ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> parseBiome((String) o, depthTracker))
                .registerLoader(Identifier.class, (type, o, loader, depthTracker) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null)
                        throw new LoadException("Invalid identifier: " + o, depthTracker);
                    return identifier;
                })
                .registerLoader(Precipitation.class, (type, o, loader, depthTracker) -> Precipitation.valueOf(((String) o).toUpperCase(
                        Locale.ROOT)))
                .registerLoader(GrassColorModifier.class, (type, o, loader, depthTracker) -> GrassColorModifier.valueOf(((String) o).toUpperCase(
                        Locale.ROOT)));
    }
    
    
    private ProtoPlatformBiome parseBiome(String id, DepthTracker tracker) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier, tracker); // failure.
        return new ProtoPlatformBiome(identifier);
    }
}
