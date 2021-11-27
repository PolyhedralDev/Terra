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

package com.dfsek.terra.fabric;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;

import com.dfsek.terra.addon.EphemeralAddon;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.util.ProtoBiome;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PlatformImpl extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformImpl.class);
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final Lazy<File> dataFolder = Lazy.lazy(() -> new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra"));
    
    private final Set<ServerWorld> worlds = new HashSet<>();
    
    public PlatformImpl() {
        load();
    }
    
    public void addWorld(ServerWorld world) {
        worlds.add(world);
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        LangUtil.load(getTerraConfig().getLanguage(), this); // Load language.
        boolean succeed = getRawConfigRegistry().loadAll(this);
        
        worlds.forEach(world -> {
            FabricChunkGeneratorWrapper chunkGeneratorWrapper = ((FabricChunkGeneratorWrapper) world.getChunkManager().getChunkGenerator());
            chunkGeneratorWrapper.setPack(getConfigRegistry().get(chunkGeneratorWrapper.getPack().getID()));
        });
        
        return succeed;
    }
    
    @Override
    protected Iterable<BaseAddon> platformAddon() {
        List<BaseAddon> addons = new ArrayList<>();
        
        addons.add(new FabricAddon(this));
        
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
        
        FabricLoader.getInstance().getAllMods().forEach(mod -> {
            if(mod.getMetadata().getId().equals("terra")) return;
            try {
                Version version = Versions.parseVersion(mod.getMetadata().getVersion().getFriendlyString());
                addons.add(new EphemeralAddon(version, "fabric:" + mod.getMetadata().getId()));
            } catch(ParseException e) {
                LOGGER.warn("Mod {}, version {} does not follow semantic versioning specification.", mod.getMetadata().getId(), mod.getMetadata().getVersion().getFriendlyString());
            }
        });
        
        return addons;
    }
    
    @Override
    public String platformName() {
        return "Fabric";
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public File getDataFolder() {
        return dataFolder.value();
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(com.dfsek.terra.api.world.biome.Biome.class, (t, o, l) -> parseBiome((String) o))
                .registerLoader(Identifier.class, (t, o, l) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null)
                        throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
    }
    
    
    private ProtoBiome parseBiome(String id) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier); // failure.
        return new ProtoBiome(identifier);
    }
}
