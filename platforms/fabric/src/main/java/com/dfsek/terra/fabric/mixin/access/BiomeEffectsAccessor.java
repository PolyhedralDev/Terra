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

package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;


@Mixin(BiomeEffects.class)
public interface BiomeEffectsAccessor {
    @Accessor("fogColor")
    int getFogColor();
    
    @Accessor("waterColor")
    int getWaterColor();
    
    @Accessor("waterFogColor")
    int getWaterFogColor();
    
    @Accessor("skyColor")
    int getSkyColor();
    
    @Accessor("foliageColor")
    Optional<Integer> getFoliageColor();
    
    @Accessor("grassColor")
    Optional<Integer> getGrassColor();
    
    @Accessor("grassColorModifier")
    BiomeEffects.GrassColorModifier getGrassColorModifier();
}
