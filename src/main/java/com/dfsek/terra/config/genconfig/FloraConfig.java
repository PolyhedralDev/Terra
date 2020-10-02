package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigObject;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FloraConfig extends TerraConfigObject implements Flora {
    private final Palette<BlockData> floraPalette;
    private final String id;
    private final boolean physics;
    
    Set<Material> spawnable;
    Set<Material> replaceable;

    public FloraConfig(File file, TerraConfig config) throws IOException, InvalidConfigurationException {
        super(file, config);
        load(file);
        if(!contains("id")) throw new ConfigException("Flora ID unspecified!", "null");
        this.id = getString("id");
        if(!contains("blocks")) throw new ConfigException("No blocks defined in custom flora!", getID());
        if(!contains("spawnable")) throw new ConfigException("Flora spawnable blocks unspecified!", getID());

        spawnable = ConfigUtil.toBlockData(getStringList("spawnable"), "spawnable", getID());
        replaceable = ConfigUtil.toBlockData(getStringList("replaceable"), "replaceable", getID());
        physics = getBoolean("physics", false);

        Palette<BlockData> p = new RandomPalette<>(new Random(getInt("seed", 4)));

        floraPalette  = PaletteConfig.getPalette(getMapList("blocks"), p);
    }

    public String getID() {
        return id;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        List<Block> blocks = new ArrayList<>();
        for(int y : range) {
            Block check = chunk.getBlock(x, y, z);
            if(spawnable.contains(check.getType())) {
                blocks.add(check);
            }
        }
        return blocks;
    }

    @Override
    public boolean plant(Location location) {
        int size = floraPalette.getSize();
        for(int i = 0; i < size; i++) {
            if(!replaceable.contains(location.clone().add(0, i+1, 0).getBlock().getType())) return false;
        }
        for(int i = 0; i < size; i++) {
            location.clone().add(0, i+1, 0).getBlock().setBlockData(floraPalette.get(size-(i+1), location.getBlockX(), location.getBlockZ()), physics);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Flora with name ID " + getID();
    }
}
