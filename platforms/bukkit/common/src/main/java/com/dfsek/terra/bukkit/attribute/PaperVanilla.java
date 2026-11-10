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
package com.dfsek.terra.bukkit.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.minecraft.attribute.VanillaContext;


public final class PaperVanilla {
    private static volatile Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerTypes;

    private PaperVanilla() { }

    public static MinecraftServer server() {
        try {
            Object server = Bukkit.getServer();
            if(server != null && server.getClass().getMethod("getServer").invoke(server) instanceof MinecraftServer minecraft) {
                return minecraft;
            }
        } catch(ReflectiveOperationException | RuntimeException e) {
            throw new IllegalStateException("not running on Paper", e);
        }
        throw new IllegalStateException("not running on Paper");
    }

    /**
     * CraftBukkit isn't on the compile classpath.
     */
    public static ServerLevel level(World world) {
        try {
            if(world.getClass().getMethod("getHandle").invoke(world) instanceof ServerLevel level) return level;
        } catch(ReflectiveOperationException e) {
            throw new IllegalStateException("not a Paper world", e);
        }
        throw new IllegalStateException("not a Paper world");
    }

    @SuppressWarnings("unchecked")
    public static Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerTypes() {
        Map<ResourceKey<Biome>, ResourceKey<VillagerType>> cached = villagerTypes;
        if(cached != null) return cached;
        synchronized(PaperVanilla.class) {
            if(villagerTypes == null) {
                Object byBiome;
                try {
                    Field field = VillagerType.class.getDeclaredField("BY_BIOME");
                    field.setAccessible(true);
                    byBiome = field.get(null);
                } catch(ReflectiveOperationException e) {
                    throw new IllegalStateException("villager types by biome are not readable", e);
                }
                if(!(byBiome instanceof HashMap<?, ?> map)) throw new IllegalStateException("villager types by biome is not a hash map");
                villagerTypes = (Map<ResourceKey<Biome>, ResourceKey<VillagerType>>) map;
            }
            return villagerTypes;
        }
    }

    public static BlockState blockState(BlockData data) {
        try {
            return (BlockState) BlockStates.GET_STATE.invokeExact(data);
        } catch(Throwable e) {
            throw new IllegalStateException("block data " + data + " has no vanilla state", e);
        }
    }

    public static VanillaContext context(HolderLookup.Provider registries) {
        return VanillaContext.of(registries, villagerTypes()::get);
    }

    /**
     * Needs CraftBukkit; resolved on first use.
     */
    private static final class BlockStates {
        private static final MethodHandle GET_STATE;

        static {
            try {
                Class<?> craft = Class.forName("org.bukkit.craftbukkit.block.data.CraftBlockData", false,
                    Bukkit.getServer().getClass().getClassLoader());
                GET_STATE = MethodHandles.publicLookup()
                    .findVirtual(craft, "getState", MethodType.methodType(BlockState.class))
                    .asType(MethodType.methodType(BlockState.class, BlockData.class));
            } catch(ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
}
