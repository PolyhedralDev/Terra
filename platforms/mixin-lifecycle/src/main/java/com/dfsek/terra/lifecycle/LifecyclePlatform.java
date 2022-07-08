package com.dfsek.terra.lifecycle;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import net.minecraft.MinecraftVersion;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.dfsek.terra.addon.EphemeralAddon;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.lifecycle.util.BiomeUtil;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;


public abstract class LifecyclePlatform extends ModPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecyclePlatform.class);
    private static MinecraftServer server;
    
    public LifecyclePlatform() {
        CommonPlatform.initialize(this);
        load();
    }
    
    @Override
    public MinecraftServer getServer() {
        return server;
    }
    
    public static void setServer(MinecraftServer server) {
        LifecyclePlatform.server = server;
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        getRawConfigRegistry().clear();
        boolean succeed = getRawConfigRegistry().loadAll(this);
        
        
        if(server != null) {
            server.reloadResources(server.getDataPackManager().getNames()).exceptionally(throwable -> {
                LOGGER.warn("Failed to execute reload", throwable);
                return null;
            }).join();
            BiomeUtil.registerBiomes();
            server.getWorlds().forEach(world -> {
                if(world.getChunkManager().getChunkGenerator() instanceof MinecraftChunkGeneratorWrapper chunkGeneratorWrapper) {
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
        
        super.platformAddon().forEach(addons::add);
        
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
    
        addons.addAll(getPlatformMods());
    
        return addons;
    }
    
    protected Stream<EphemeralAddon> parseModData(String id, String modVersion) {
        if(id.equals("terra") || id.equals("minecraft") || id.equals("java")) return Stream.empty();
        try {
            Version version = Versions.parseVersion(modVersion);
            return Stream.of(new EphemeralAddon(version, "quilt:" + id));
        } catch(ParseException e) {
            LOGGER.warn(
                    "Mod {}, version {} does not follow semantic versioning specification, Terra addons will be unable to depend on " +
                    "it.",
                    id, modVersion);
        }
        return Stream.empty();
    }
    
    protected abstract Collection<BaseAddon> getPlatformMods();
}
