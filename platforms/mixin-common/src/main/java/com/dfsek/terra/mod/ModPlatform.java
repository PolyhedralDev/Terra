package com.dfsek.terra.mod;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.mod.attribute.MinecraftAttributeParents;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.handle.MinecraftItemHandle;
import com.dfsek.terra.mod.handle.MinecraftWorldHandle;
import com.dfsek.terra.mod.util.PresetUtil;


public abstract class ModPlatform extends AbstractPlatform {
    private final ItemHandle itemHandle = new MinecraftItemHandle();
    private final WorldHandle worldHandle = new MinecraftWorldHandle();
    private final MinecraftAttributeParents attributeParents = new MinecraftAttributeParents(getAttributeHandle());

    public abstract MinecraftServer getServer();

    public void registerWorldTypes(BiConsumer<Identifier, WorldPreset> registerFunction) {
        HashSet<String> configPacksInMetaPack = new HashSet<>();
        getRawMetaConfigRegistry().forEach(pack -> {
            PresetUtil.createMetaPackPreset(pack, this, false).apply(registerFunction);
            pack.packs().forEach((k, v) -> configPacksInMetaPack.add(v.getID()));
        });
        getRawConfigRegistry()
            .forEach(pack -> {
                boolean packInMetapack = configPacksInMetaPack.contains(pack.getID());
                PresetUtil.createDefault(pack, this, packInMetapack, packInMetapack).apply(registerFunction);
            });
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
            });
    }

    private ProtoPlatformBiome parseBiome(String id, DepthTracker tracker) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(!biomeRegistry().containsKey(identifier)) throw new LoadException("Invalid Biome ID: " + identifier, tracker); // failure.
        return new ProtoPlatformBiome(identifier);
    }

    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(getPlatformAddon());
    }

    protected abstract BaseAddon getPlatformAddon();

    public abstract Registry<DimensionType> dimensionTypeRegistry();

    public abstract Registry<Biome> biomeRegistry();

    public abstract Registry<NoiseGeneratorSettings> chunkGeneratorSettingsRegistry();

    public abstract Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterListRegistry();

    public abstract Registry<Enchantment> enchantmentRegistry();

    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public @NotNull ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public @NotNull AttributeParents getAttributeParents() {
        return attributeParents;
    }

    public MinecraftAttributeParents attributeParents() {
        return attributeParents;
    }


}
