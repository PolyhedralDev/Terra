package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import net.jafama.FastMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FloraConfig extends TerraConfig implements Flora {
    private final Palette<BlockData> floraPalette;
    private final String id;
    private final boolean physics;
    private final boolean ceiling;

    private final Set<Material> irrigable;

    private final Set<Material> spawnable;
    private final Set<Material> replaceable;

    public FloraConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        load(file);
        if(!contains("id")) throw new ConfigException("Flora ID unspecified!", "null");
        this.id = getString("id");
        if(!contains("layers")) throw new ConfigException("No blocks defined in custom flora!", getID());
        if(!contains("spawnable")) throw new ConfigException("Flora spawnable blocks unspecified!", getID());

        spawnable = ConfigUtil.toBlockData(getStringList("spawnable"), "spawnable", getID());
        replaceable = ConfigUtil.toBlockData(getStringList("replaceable"), "replaceable", getID());

        if(contains("irrigable")) {
            irrigable = ConfigUtil.toBlockData(getStringList("irrigable"), "irrigable", getID());
        } else irrigable = null;

        physics = getBoolean("physics", false);
        ceiling = getBoolean("ceiling", false);

        Palette<BlockData> p = new RandomPalette<>(new FastRandom(getInt("seed", 4)));

        floraPalette = PaletteConfig.getPalette(getMapList("layers"), p);
    }

    public String getID() {
        return id;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        int size = floraPalette.getSize();
        Block current = chunk.getBlock(x, range.getMin(), z);
        List<Block> blocks = new ArrayList<>();
        for(int y : range) {
            if(y > 255 || y < 0) continue;
            current = current.getRelative(BlockFace.UP);
            if(spawnable.contains(current.getType()) && isIrrigated(current) && valid(size, current)) {
                blocks.add(current);
            }
        }
        return blocks;
    }

    private boolean valid(int size, Block block) {
        for(int i = 0; i < size; i++) { // Down if ceiling, up if floor
            if(block.getY() + 1 > 255 || block.getY() < 0) return false;
            block = block.getRelative(ceiling ? BlockFace.DOWN : BlockFace.UP);
            if(!replaceable.contains(block.getType())) return false;
        }
        return true;
    }

    private boolean isIrrigated(Block b) {
        if(irrigable == null) return true;
        return irrigable.contains(b.getRelative(BlockFace.NORTH).getType())
                || irrigable.contains(b.getRelative(BlockFace.SOUTH).getType())
                || irrigable.contains(b.getRelative(BlockFace.EAST).getType())
                || irrigable.contains(b.getRelative(BlockFace.WEST).getType());
    }

    @Override
    public boolean plant(Location location) {
        int size = floraPalette.getSize();
        int c = ceiling ? -1 : 1;
        for(int i = 0; FastMath.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (FastMath.abs(i));
            location.clone().add(0, i + c, 0).getBlock().setBlockData(floraPalette.get((ceiling ? lvl : size - lvl - 1), location.getBlockX(), location.getBlockZ()), physics);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Flora with name ID " + getID();
    }
}
