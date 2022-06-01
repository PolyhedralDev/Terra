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

package com.dfsek.terra.sponge.handle;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.BlockTypes;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.sponge.block.SpongeBlockState;


public class SpongeWorldHandle implements WorldHandle {
    private final Lazy<SpongeBlockState> air;
    
    public SpongeWorldHandle() {
        air = Lazy.lazy(() -> new SpongeBlockState(BlockTypes.AIR.get().defaultState()));
    }
    
    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        return new SpongeBlockState(org.spongepowered.api.block.BlockState.fromString(data));
    }
    
    @Override
    public @NotNull BlockState air() {
        return air.value();
    }
    
    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        throw new UnsupportedOperationException();
    }
}
