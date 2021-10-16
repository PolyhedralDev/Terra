package com.dfsek.terra.addons.sponge;

import com.dfsek.terra.api.block.state.BlockState;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTUtil;
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

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.Structure;


public class SpongeSchematicAddon extends TerraAddon {
    @Inject
    private Platform platform;
    
    
    
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    CheckedRegistry<Structure> structureRegistry = event.getPack().getOrCreateRegistry(Structure.class);
                    event.getPack().getLoader().open("", ".schem").thenEntries(entries -> {
                        for(Map.Entry<String, InputStream> entry : entries) {
                        
                        }
                    }).close();
                })
                .failThrough();
    }
    
    
    public String convert(InputStream in) {
        try {
            CompoundTag baseTag = (CompoundTag) new NBTDeserializer(false).fromStream(detectDecompression(in)).getTag();
            int wid = baseTag.getShort("Width");
            int len = baseTag.getShort("Length");
            int hei = baseTag.getShort("Height");
            
            ByteArrayTag blocks = baseTag.getByteArrayTag("BlockData");
            
            CompoundTag palette = (CompoundTag) baseTag.get("Palette");
            Map<Integer, String> data = new HashMap<>();
            
            for(Map.Entry<String, Tag<?>> entry : palette.entrySet()) {
                data.put(((IntTag) entry.getValue()).asInt(), entry.getKey());
            }
            
            byte[] arr =  blocks.getValue();
            ScriptBuilder builder = new ScriptBuilder();
            for(int x = 0; x < hei; x++) {
                for(int y = 0; y < wid; y++) {
                    for(int z = 0; z < len; z++) {
                        String block = data.get((int) arr[x+z*wid+y*wid*len]);
                        if(block.startsWith("minecraft:structure_void")) continue;
                        builder.block(x, y, z, block);
                    }
                }
            }
            
            return builder.build();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static InputStream detectDecompression(InputStream is) throws IOException {
        PushbackInputStream pbis = new PushbackInputStream(is, 2);
        int signature = (pbis.read() & 0xFF) + (pbis.read() << 8);
        pbis.unread(signature >> 8);
        pbis.unread(signature & 0xFF);
        if (signature == GZIPInputStream.GZIP_MAGIC) {
            return new GZIPInputStream(pbis);
        }
        return pbis;
    }
}
