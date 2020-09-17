package com.dfsek.terra.config;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.BlockPalette;
import org.polydev.gaea.world.Fauna;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FaunaConfig extends YamlConfiguration implements Fauna {
    private BlockPalette faunaPalette = new BlockPalette();
    private String id;
    private String friendlyName;
    
    List<Material> spawnable;
    public FaunaConfig(File file) throws IOException, InvalidConfigurationException {
        super();
        load(file);
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        if(!contains("blocks")) throw new InvalidConfigurationException("No blocks defined in custom fauna!");
        if(!contains("id")) throw new InvalidConfigurationException("Fauna ID unspecified!");
        if(!contains("name")) throw new InvalidConfigurationException("Fauna name unspecified!");
        if(!contains("spawnable")) throw new InvalidConfigurationException("Fauna spawnable blocks unspecified!");

        try {
            spawnable = ConfigUtil.getElements(getStringList("spawnable"), Material.class);
            Bukkit.getLogger().info("[Terra] Added " + spawnable.size() + " items to spawnable list.");
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid material ID in spawnable list: " + e.getMessage());
        }

        faunaPalette  = PaletteConfig.getPalette(getMapList("blocks"));
        if(!contains("id")) throw new InvalidConfigurationException("Fauna ID unspecified!");
        this.id = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Fauna Name unspecified!");
        this.friendlyName = getString("name");
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getID() {
        return id;
    }

    @Override
    public Block getHighestValidSpawnAt(Chunk chunk, int x, int z) {
        int y;
        for(y = chunk.getWorld().getMaxHeight() - 1; (!spawnable.contains(chunk.getBlock(x, y, z).getType())) && y > 0; y--);
        if(y <= 0) return null;
        return chunk.getBlock(x, y, z);
    }

    @Override
    public boolean plant(Location location) {
        int size = faunaPalette.getSize();
        for(int i = 0; i < size; i++) {
            if(!location.clone().add(0, i+1, 0).getBlock().isEmpty()) return false;
        }
        for(int i = 0; i < size; i++) {
            location.clone().add(0, i+1, 0).getBlock().setBlockData(faunaPalette.getBlockData(size-(i+1), new Random()), false);
        }
        return true;
    }

}
