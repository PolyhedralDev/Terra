/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.sponge;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.tag.ByteArrayTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.StringUtil;
import com.dfsek.terra.api.util.vector.Vector3Int;


public class SpongeSchematicAddon implements AddonInitializer {
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    private static InputStream detectDecompression(InputStream is) throws IOException {
        PushbackInputStream pbis = new PushbackInputStream(is, 2);
        int signature = (pbis.read() & 0xFF) + (pbis.read() << 8);
        pbis.unread(signature >> 8);
        pbis.unread(signature & 0xFF);
        if(signature == GZIPInputStream.GZIP_MAGIC) {
            return new GZIPInputStream(pbis);
        }
        return pbis;
    }

    @Override
    public void initialize() {
        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPreLoadEvent.class)
            .then(event -> {
                CheckedRegistry<Structure> structureRegistry = event.getPack().getOrCreateRegistry(Structure.class);
                event.getPack()
                    .getLoader()
                    .open("", ".schem")
                    .thenEntries(entries -> entries
                        .stream()
                        .map(entry -> convert(entry.getValue(), StringUtil.fileName(entry.getKey())))
                        .forEach(structureRegistry::register)).close();
            })
            .failThrough();
    }

    public SpongeStructure convert(InputStream in, String id) {
        try {
            CompoundTag baseTag = (CompoundTag) new NBTDeserializer(false).fromStream(detectDecompression(in)).getTag();
            int ver = baseTag.getInt("Version");
            int wid = baseTag.getShort("Width");
            int len = baseTag.getShort("Length");
            int hei = baseTag.getShort("Height");

            CompoundTag metadata = baseTag.getCompoundTag("Metadata");

            Vector3Int offset = switch(ver) {
                case 2 -> {
                    // Use WorldEdit defined legacy relative offset if it exists in schematic metadata
                    IntTag worldEditOffsetX = metadata.getIntTag("WEOffsetX");
                    IntTag worldEditOffsetY = metadata.getIntTag("WEOffsetY");
                    IntTag worldEditOffsetZ = metadata.getIntTag("WEOffsetZ");
                    if(worldEditOffsetX != null || worldEditOffsetY != null || worldEditOffsetZ != null) {
                        if(worldEditOffsetX == null || worldEditOffsetY == null || worldEditOffsetZ == null) {
                            throw new IllegalArgumentException("Failed to parse Sponge schematic: Malformed WorldEdit offset");
                        }
                        yield Vector3Int.of(worldEditOffsetX.asInt(), worldEditOffsetY.asInt(), worldEditOffsetZ.asInt());
                    } else {
                        // Relative offset handling via 'Offset' field is ambiguous in spec 2 so just apply no offset
                        yield Vector3Int.zero();
                    }
                }
                case 3 -> {
                    // Relative offset is more concretely defined in spec 3 to use 'Offset' field
                    int[] offsetArray = baseTag.getIntArray("Offset");
                    yield Vector3Int.of(offsetArray[0], offsetArray[1], offsetArray[2]);
                }
                default -> throw new IllegalArgumentException("Failed to parse Sponge schematic: Unsupported format version: " + ver);
            };

            ByteArrayTag blocks = baseTag.getByteArrayTag("BlockData");

            CompoundTag palette = (CompoundTag) baseTag.get("Palette");
            Map<Integer, String> data = new HashMap<>();

            for(Map.Entry<String, Tag<?>> entry : palette.entrySet()) {
                data.put(((IntTag) entry.getValue()).asInt(), entry.getKey());
            }

            BlockState[][][] states = new BlockState[wid][len][hei];

            byte[] arr = blocks.getValue();
            for(int x = 0; x < wid; x++) {
                for(int z = 0; z < len; z++) {
                    for(int y = 0; y < hei; y++) {
                        String block = data.get((int) arr[x + z * wid + y * wid * len]);
                        if(block.startsWith("minecraft:structure_void")) continue;
                        states[x][z][y] = platform.getWorldHandle().createBlockState(block);
                    }
                }
            }

            return new SpongeStructure(states, offset, addon.key(id));
        } catch(IOException e) {
            throw new IllegalArgumentException("Failed to parse Sponge schematic: ", e);
        }
    }
}
