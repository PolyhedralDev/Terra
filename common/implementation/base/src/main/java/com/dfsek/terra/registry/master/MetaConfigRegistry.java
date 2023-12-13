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

package com.dfsek.terra.registry.master;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.MetaPack;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.config.pack.MetaPackImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.master.ConfigRegistry.PackLoadFailuresException;


/**
 * Class to hold config packs
 */
public class MetaConfigRegistry extends OpenRegistryImpl<MetaPack> {

    public MetaConfigRegistry() {
        super(TypeKey.of(MetaPack.class));
    }

    public void loadAll(Platform platform, ConfigRegistry configRegistry) throws IOException, PackLoadFailuresException {
        Path packsDirectory = platform.getDataFolder().toPath().resolve("metapacks");
        Files.createDirectories(packsDirectory);
        List<IOException> failedLoads = new ArrayList<>();
        try(Stream<Path> packs = Files.list(packsDirectory)) {
            packs.forEach(path -> {
                try {
                    MetaPack pack = new MetaPackImpl(path, platform, configRegistry);
                    registerChecked(pack.getRegistryKey(), pack);
                } catch(IOException e) {
                    failedLoads.add(e);
                }
            });
        }
        if(!failedLoads.isEmpty()) {
            throw new PackLoadFailuresException(failedLoads);
        }
    }
}
