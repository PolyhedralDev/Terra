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
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;


/**
 * Class to hold config packs
 */
public class ConfigRegistry extends OpenRegistryImpl<ConfigPack> {

    public ConfigRegistry() {
        super(TypeKey.of(ConfigPack.class));
    }

    public void loadAll(Platform platform) throws IOException, PackLoadFailuresException {
        Path packsDirectory = platform.getDataFolder().toPath().resolve("packs");
        Files.createDirectories(packsDirectory);
        List<IOException> failedLoads = new ArrayList<>();
        try (Stream<Path> packs = Files.list(packsDirectory)) {
            packs.forEach(path -> {
                try {
                    ConfigPack pack = new ConfigPackImpl(path, platform);
                    registerChecked(pack.getRegistryKey(), pack);
                } catch (IOException e) {
                    failedLoads.add(e);
                }
            });
        }
        if (!failedLoads.isEmpty()) {
            throw new PackLoadFailuresException(failedLoads);
        }
    }

    public static class PackLoadFailuresException extends Exception {
        @Serial
        private static final long serialVersionUID = 538998844645186306L;

        private final List<Throwable> exceptions;

        public PackLoadFailuresException(List<? extends Throwable> exceptions) {
            this.exceptions = (List<Throwable>) exceptions;
        }

        public List<Throwable> getExceptions() {
            return exceptions;
        }
    }
}
