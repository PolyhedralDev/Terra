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

package com.dfsek.terra.bukkit.world.block.data;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.loot.LootTables;
import org.bukkit.loot.Lootable;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitBlockState implements BlockState {
    private static final Set<String> SUPPORTED_BLOCK_ENTITY_TAGS = Set.of("LootTable", "LootTableSeed");
    private final org.bukkit.block.data.BlockData delegate;
    private final CompoundBinaryTag blockEntityData;

    protected BukkitBlockState(org.bukkit.block.data.BlockData delegate) {
        this(delegate, null);
    }

    protected BukkitBlockState(org.bukkit.block.data.BlockData delegate, CompoundBinaryTag blockEntityData) {
        this.delegate = delegate;
        this.blockEntityData = blockEntityData;
    }

    public static BlockState newInstance(org.bukkit.block.data.BlockData bukkitData) {
        return new BukkitBlockState(bukkitData);
    }

    public static BlockState newInstance(org.bukkit.block.data.BlockData bukkitData, CompoundBinaryTag blockEntityData) {
        return new BukkitBlockState(bukkitData, blockEntityData);
    }

    public void applyBlockEntityData(org.bukkit.block.BlockState state) {
        if(blockEntityData == null || blockEntityData.isEmpty()) return;

        Set<String> unsupported = new HashSet<>(blockEntityData.keySet());
        unsupported.removeAll(SUPPORTED_BLOCK_ENTITY_TAGS);
        if(!unsupported.isEmpty()) throw new IllegalArgumentException("Unsupported block entity tags: " + unsupported);
        if(!blockEntityData.contains("LootTable")) return;
        if(!(state instanceof Lootable lootable)) {
            throw new IllegalArgumentException(state.getType() + " does not support a loot table");
        }

        String id = blockEntityData.getString("LootTable");
        NamespacedKey key = id.indexOf(':') < 0 ? NamespacedKey.minecraft(id) : NamespacedKey.fromString(id);
        if(key == null) throw new IllegalArgumentException("Invalid loot table identifier: " + id);
        LootTables lootTables = Registry.LOOT_TABLES.get(key);
        if(lootTables == null) throw new IllegalArgumentException("Unknown loot table: " + key);

        lootable.setLootTable(lootTables.getLootTable(), blockEntityData.getLong("LootTableSeed", 0L));
        state.update(true, false);
    }


    @Override
    public org.bukkit.block.data.BlockData getHandle() {
        return delegate;
    }

    @Override
    public boolean matches(BlockState data) {
        return delegate.getMaterial() == ((BukkitBlockState) data).getHandle().getMaterial();
    }

    @Override
    public <T extends Comparable<T>> boolean has(Property<T> property) {
        return false;
    }

    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        return null;
    }

    @Override
    public <T extends Comparable<T>> BlockState set(Property<T> property, T value) {
        return null;
    }

    @Override
    public BlockType getBlockType() {
        return BukkitAdapter.adapt(delegate.getMaterial());
    }

    @Override
    public String getAsString(boolean properties) {
        return delegate.getAsString(!properties);
    }

    @Override
    public boolean isAir() {
        return delegate.getMaterial().isAir();
    }
}
