package com.dfsek.terra.mod.util;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.config.MonsterSettingsConfig;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.config.VanillaWorldProperties;

import com.dfsek.terra.mod.implmentation.TerraIntProvider;

import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionType.MonsterSettings;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalLong;


public class DimensionUtil {
    public static DimensionType createDimension(ConfigPack pack, ModPlatform platform) {
        VanillaWorldProperties vanillaWorldProperties;
        MonsterSettingsConfig monsterSettingsConfig;
        if (pack.getContext().has(VanillaBiomeProperties.class)) {
            vanillaWorldProperties = pack.getContext().get(VanillaWorldProperties.class);
        } else {
            vanillaWorldProperties = new VanillaWorldProperties();
        }
        if (vanillaWorldProperties.getMonsterSettings() != null) {
            monsterSettingsConfig = vanillaWorldProperties.getMonsterSettings();
        } else {
            monsterSettingsConfig = new MonsterSettingsConfig();
        }

        Registry<DimensionType> dimensionTypeRegistry = platform.dimensionTypeRegistry();

        DimensionType defaultDimension = dimensionTypeRegistry.get(new Identifier(vanillaWorldProperties.getVanillaDimension()));

        assert defaultDimension != null;
        MonsterSettings monsterSettings = getMonsterSettings(defaultDimension, monsterSettingsConfig);

        DimensionType dimension = new DimensionType(
            vanillaWorldProperties.getFixedTime() == null ? defaultDimension.fixedTime() : OptionalLong.of(
                vanillaWorldProperties.getFixedTime()),
            vanillaWorldProperties.getHasSkyLight() == null ? defaultDimension.hasSkyLight() : vanillaWorldProperties.getHasSkyLight(),
            vanillaWorldProperties.getHasCeiling() == null ? defaultDimension.hasCeiling() : vanillaWorldProperties.getHasCeiling(),
            vanillaWorldProperties.getUltraWarm() == null ? defaultDimension.ultrawarm() : vanillaWorldProperties.getUltraWarm(),
            vanillaWorldProperties.getNatural() == null ? defaultDimension.natural() : vanillaWorldProperties.getNatural(),
            vanillaWorldProperties.getCoordinateScale() == null ? defaultDimension.coordinateScale() : vanillaWorldProperties.getCoordinateScale(),
            vanillaWorldProperties.getBedWorks() == null ? defaultDimension.bedWorks() : vanillaWorldProperties.getBedWorks(),
            vanillaWorldProperties.getRespawnAnchorWorks() == null ? defaultDimension.respawnAnchorWorks() : vanillaWorldProperties.getRespawnAnchorWorks(),
            vanillaWorldProperties.getHeight() == null ? defaultDimension.minY() : vanillaWorldProperties.getHeight().getMin(),
            vanillaWorldProperties.getHeight() == null ? defaultDimension.height() : vanillaWorldProperties.getHeight().getMax(),
            vanillaWorldProperties.getLogicalHeight() == null ? defaultDimension.logicalHeight() : vanillaWorldProperties.getLogicalHeight(),
            vanillaWorldProperties.getInfiniburn() == null ? defaultDimension.infiniburn() : TagKey.of(RegistryKeys.BLOCK, vanillaWorldProperties.getInfiniburn()),
            vanillaWorldProperties.getEffects() == null ? defaultDimension.effects() : vanillaWorldProperties.getEffects(),
            vanillaWorldProperties.getAmbientLight() == null ? defaultDimension.ambientLight() : vanillaWorldProperties.getAmbientLight(),
            monsterSettings
        );

        return dimension;
    }

    @NotNull
    private static MonsterSettings getMonsterSettings(DimensionType defaultDimension, MonsterSettingsConfig monsterSettingsConfig) {
        MonsterSettings defaultMonsterSettings = defaultDimension.monsterSettings();


        MonsterSettings monsterSettings = new MonsterSettings(
            monsterSettingsConfig.getPiglinSafe() == null ? defaultMonsterSettings.piglinSafe() : monsterSettingsConfig.getPiglinSafe(),
            monsterSettingsConfig.getHasRaids() == null ? defaultMonsterSettings.hasRaids() : monsterSettingsConfig.getHasRaids(),
            monsterSettingsConfig.getMonsterSpawnLight() == null ? defaultMonsterSettings.monsterSpawnLightTest() : new TerraIntProvider(
                monsterSettingsConfig.getMonsterSpawnLight()),
            monsterSettingsConfig.getMonsterSpawnBlockLightLimit() == null ? defaultMonsterSettings.monsterSpawnBlockLightLimit() : monsterSettingsConfig.getMonsterSpawnBlockLightLimit()
        );
        return monsterSettings;
    }
}
