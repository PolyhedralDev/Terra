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

package com.dfsek.terra.bukkit.world.block.state;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.block.entity.Sign;


@SuppressWarnings("deprecation")
public class BukkitSign extends BukkitBlockEntity implements Sign {
    protected BukkitSign(org.bukkit.block.Sign block) {
        super(block);
    }
    
    @Override
    public void setLine(int index, @NonNull String line) throws IndexOutOfBoundsException {
        ((org.bukkit.block.Sign) getHandle()).setLine(index, line);
    }
    
    @Override
    public @NonNull String[] getLines() {
        return ((org.bukkit.block.Sign) getHandle()).getLines();
    }
    
    @Override
    public @NonNull String getLine(int index) throws IndexOutOfBoundsException {
        return ((org.bukkit.block.Sign) getHandle()).getLine(index);
    }
    
    @Override
    public void applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
