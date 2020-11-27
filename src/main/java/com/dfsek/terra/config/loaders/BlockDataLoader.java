package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import java.lang.reflect.Type;

public class BlockDataLoader implements TypeLoader<BlockData> {
    @Override
    public BlockData load(Type type, Object o, ConfigLoader configLoader) {
        return Bukkit.createBlockData((String) o);
    }
}
