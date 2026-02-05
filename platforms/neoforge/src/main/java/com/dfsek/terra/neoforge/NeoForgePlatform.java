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

package com.dfsek.terra.neoforge;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.lifecycle.LifecyclePlatform;


public class NeoForgePlatform extends LifecyclePlatform {

    @Override
    protected Collection<BaseAddon> getPlatformMods() {
        return ModList.get().getMods().stream().flatMap(
            mod -> parseModData(mod.getModId(), mod.getVersion().toString(), "neoforge")
        ).collect(Collectors.toList());
    }

    @Override
    public @NotNull String platformName() {
        return "NeoForge";
    }

    @Override
    public @NotNull File getDataFolder() {
        return new File(FMLPaths.CONFIGDIR.get().toFile(), "Terra");
    }

    @Override
    public BaseAddon getPlatformAddon() {
        return new NeoForgeAddon(this);
    }
}
