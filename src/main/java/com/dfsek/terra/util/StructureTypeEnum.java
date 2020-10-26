package com.dfsek.terra.util;

import org.bukkit.StructureType;

/**
 * Enum to represent StructureType, which is a class for some reason.
 */
public enum StructureTypeEnum {
    MINESHAFT(StructureType.MINESHAFT),
    VILLAGE(StructureType.VILLAGE),
    NETHER_FORTRESS(StructureType.NETHER_FORTRESS),
    STRONGHOLD(StructureType.STRONGHOLD),
    JUNGLE_PYRAMID(StructureType.JUNGLE_PYRAMID),
    OCEAN_RUIN(StructureType.OCEAN_RUIN),
    DESERT_PYRAMID(StructureType.DESERT_PYRAMID),
    IGLOO(StructureType.IGLOO),
    SWAMP_HUT(StructureType.SWAMP_HUT),
    OCEAN_MONUMENT(StructureType.OCEAN_MONUMENT),
    END_CITY(StructureType.END_CITY),
    WOODLAND_MANSION(StructureType.WOODLAND_MANSION),
    BURIED_TREASURE(StructureType.BURIED_TREASURE),
    SHIPWRECK(StructureType.SHIPWRECK),
    PILLAGER_OUTPOST(StructureType.PILLAGER_OUTPOST),
    NETHER_FOSSIL(StructureType.NETHER_FOSSIL),
    RUINED_PORTAL(StructureType.RUINED_PORTAL),
    BASTION_REMNANT(StructureType.BASTION_REMNANT);

    StructureTypeEnum(StructureType type) {
        this.type = type;
    }

    private final StructureType type;

    public StructureType getType() {
        return type;
    }
}
