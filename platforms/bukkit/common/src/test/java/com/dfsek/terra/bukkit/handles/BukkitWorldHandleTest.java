package com.dfsek.terra.bukkit.handles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class BukkitWorldHandleTest {
    @Test
    void parsesLegacyBlockEntitySnbt() {
        var data = BukkitWorldHandle.parseBlockEntityData("{LootTable:'archaeology/trial_ruins_rare',LootTableSeed:42L}");

        assertEquals("archaeology/trial_ruins_rare", data.getString("LootTable"));
        assertEquals(42L, data.getLong("LootTableSeed"));
    }

    @Test
    void rejectsInvalidBlockEntitySnbt() {
        assertThrows(IllegalArgumentException.class, () -> BukkitWorldHandle.parseBlockEntityData("{LootTable:"));
    }
}
