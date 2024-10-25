package com.dfsek.terra.mod.util;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionType.MonsterSettings;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalLong;

import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.config.MonsterSettingsConfig;
import com.dfsek.terra.mod.config.VanillaWorldProperties;
import com.dfsek.terra.mod.implmentation.TerraIntProvider;


public class DimensionUtil {
    public static DimensionType createDimension(VanillaWorldProperties vanillaWorldProperties, DimensionType defaultDimension,
                                                ModPlatform platform) {

        MonsterSettingsConfig monsterSettingsConfig;
        if(vanillaWorldProperties.getMonsterSettings() != null) {
            monsterSettingsConfig = vanillaWorldProperties.getMonsterSettings();
        } else {
            monsterSettingsConfig = new MonsterSettingsConfig();
        }

        MonsterSettings monsterSettings = getMonsterSettings(defaultDimension, monsterSettingsConfig);

        return new DimensionType(
            vanillaWorldProperties.getFixedTime() == null ? defaultDimension.fixedTime() : OptionalLong.of(
                vanillaWorldProperties.getFixedTime()),
            vanillaWorldProperties.getHasSkyLight() == null ? defaultDimension.hasSkyLight() : vanillaWorldProperties.getHasSkyLight(),
            vanillaWorldProperties.getHasCeiling() == null ? defaultDimension.hasCeiling() : vanillaWorldProperties.getHasCeiling(),
            vanillaWorldProperties.getUltraWarm() == null ? defaultDimension.ultrawarm() : vanillaWorldProperties.getUltraWarm(),
            vanillaWorldProperties.getNatural() == null ? defaultDimension.natural() : vanillaWorldProperties.getNatural(),
            vanillaWorldProperties.getCoordinateScale() == null
            ? defaultDimension.coordinateScale()
            : vanillaWorldProperties.getCoordinateScale(),
            vanillaWorldProperties.getBedWorks() == null ? defaultDimension.bedWorks() : vanillaWorldProperties.getBedWorks(),
            vanillaWorldProperties.getRespawnAnchorWorks() == null
            ? defaultDimension.respawnAnchorWorks()
            : vanillaWorldProperties.getRespawnAnchorWorks(),
            vanillaWorldProperties.getHeight() == null ? defaultDimension.minY() : vanillaWorldProperties.getHeight().getMin(),
            vanillaWorldProperties.getHeight() == null ? defaultDimension.height() : vanillaWorldProperties.getHeight().getRange(),
            vanillaWorldProperties.getLogicalHeight() == null
            ? defaultDimension.logicalHeight()
            : vanillaWorldProperties.getLogicalHeight(),
            vanillaWorldProperties.getInfiniburn() == null
            ? defaultDimension.infiniburn()
            : TagKey.of(RegistryKeys.BLOCK, vanillaWorldProperties.getInfiniburn()),
            vanillaWorldProperties.getEffects() == null ? defaultDimension.effects() : vanillaWorldProperties.getEffects(),
            vanillaWorldProperties.getAmbientLight() == null ? defaultDimension.ambientLight() : vanillaWorldProperties.getAmbientLight(),
            monsterSettings
        );
    }

    @NotNull
    private static MonsterSettings getMonsterSettings(DimensionType defaultDimension, MonsterSettingsConfig monsterSettingsConfig) {
        MonsterSettings defaultMonsterSettings = defaultDimension.monsterSettings();


        return new MonsterSettings(
            monsterSettingsConfig.getPiglinSafe() == null ? defaultMonsterSettings.piglinSafe() : monsterSettingsConfig.getPiglinSafe(),
            monsterSettingsConfig.getHasRaids() == null ? defaultMonsterSettings.hasRaids() : monsterSettingsConfig.getHasRaids(),
            monsterSettingsConfig.getMonsterSpawnLight() == null ? defaultMonsterSettings.monsterSpawnLightTest() : new TerraIntProvider(
                monsterSettingsConfig.getMonsterSpawnLight()),
            monsterSettingsConfig.getMonsterSpawnBlockLightLimit() == null
            ? defaultMonsterSettings.monsterSpawnBlockLightLimit()
            : monsterSettingsConfig.getMonsterSpawnBlockLightLimit()
        );
    }
}
