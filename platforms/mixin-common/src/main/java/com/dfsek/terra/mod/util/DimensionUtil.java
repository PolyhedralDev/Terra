package com.dfsek.terra.mod.util;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.config.VanillaWorldProperties;


public class DimensionUtil {
    protected static RegistryKey<DimensionType> registerDimension(Identifier identifier,
                                                                  DimensionType dimension) {
        BuiltinRegistries.add(BuiltinRegistries.DIMENSION_TYPE,
                              registerKey(identifier)
                                      .getValue(),
                              dimension);
        return getDimensionKey(identifier);
    }
    
    public static RegistryKey<DimensionOptions> registerKey(Identifier identifier) {
        return RegistryKey.of(Registry.DIMENSION_KEY, identifier);
    }
    
    public static RegistryKey<DimensionType> getDimensionKey(Identifier identifier) {
        return BuiltinRegistries.DIMENSION_TYPE.getKey(BuiltinRegistries.DIMENSION_TYPE.get(identifier)).orElseThrow();
    }
    
    protected static RegistryKey<DimensionType> registerDimension(ConfigPack pack) {
        VanillaWorldProperties vanillaWorldProperties;
        if(pack.getContext().has(VanillaBiomeProperties.class)) {
            vanillaWorldProperties = pack.getContext().get(VanillaWorldProperties.class);
        } else {
            vanillaWorldProperties = new VanillaWorldProperties();
        }
        
        DimensionType overworldDimensionType = new DimensionType(
                vanillaWorldProperties.getFixedTime() == null
                ? OptionalLong.empty()
                : OptionalLong.of(vanillaWorldProperties.getFixedTime()),
                vanillaWorldProperties.getHasSkyLight(),
                vanillaWorldProperties.getHasCeiling(),
                vanillaWorldProperties.getUltraWarm(),
                vanillaWorldProperties.getNatural(),
                vanillaWorldProperties.getCoordinateScale(),
                vanillaWorldProperties.getBedWorks(),
                vanillaWorldProperties.getRespawnAnchorWorks(),
                vanillaWorldProperties.getHeight().getMin(),
                vanillaWorldProperties.getHeight().getMax(),
                vanillaWorldProperties.getLogicalHeight(),
                TagKey.of(Registry.BLOCK_KEY, vanillaWorldProperties.getInfiniburn()),
                vanillaWorldProperties.getEffects(),
                vanillaWorldProperties.getAmbientLight(),
                vanillaWorldProperties.getMonsterSettings());
        
        return registerDimension(new Identifier("terra", pack.getID().toLowerCase()), overworldDimensionType);
    }
}
