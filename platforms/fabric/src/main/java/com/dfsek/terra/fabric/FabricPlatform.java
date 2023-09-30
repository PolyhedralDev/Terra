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

package com.dfsek.terra.fabric;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.lifecycle.LifecyclePlatform;


public class FabricPlatform extends LifecyclePlatform {
    
    @Override
    protected Collection<BaseAddon> getPlatformMods() {
        return FabricLoader.getInstance().getAllMods().stream().flatMap(
                mod -> parseModData(mod.getMetadata().getId(), mod.getMetadata().getVersion().getFriendlyString())).collect(
                Collectors.toList());
    }
    
    @Override
    public @NotNull String platformName() {
        return "Fabric";
    }
    
    @Override
    public @NotNull File getDataFolder() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra");
    }
    
    @Override
    public BaseAddon getPlatformAddon() {
        return new FabricAddon(this);
    }
}
