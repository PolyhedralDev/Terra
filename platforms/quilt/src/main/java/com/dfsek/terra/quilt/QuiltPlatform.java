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

package com.dfsek.terra.quilt;

import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.QuiltLoader;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.lifecycle.LifecyclePlatform;


public class QuiltPlatform extends LifecyclePlatform {

    @Override
    protected Collection<BaseAddon> getPlatformMods() {
        return QuiltLoader.getAllMods()
            .stream()
            .flatMap(mod -> parseModData(mod.metadata().id(), mod.metadata().version().raw(), "quilt"))
            .collect(
                Collectors.toList());
    }

    @Override
    public @NotNull String platformName() {
        return "Quilt";
    }

    @Override
    public @NotNull File getDataFolder() {
        return new File(QuiltLoader.getConfigDir().toFile(), "Terra");
    }

    @Override
    public BaseAddon getPlatformAddon() {
        return new QuiltAddon(this);
    }
}
