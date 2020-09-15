package com.dfsek.terra;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.tree.Tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum TerraTree implements Tree {
    SPRUCE_SMALL("/trees/spruce_small/s_spruce_", 4, Arrays.asList(Material.PODZOL, Material.GRASS_BLOCK, Material.DIRT)),
    SPRUCE_MEDIUM("/trees/spruce_medium/m_spruce_", 5, Arrays.asList(Material.PODZOL, Material.GRASS_BLOCK, Material.DIRT)),
    SPRUCE_LARGE("/trees/spruce_large/l_spruce_", 2, Arrays.asList(Material.PODZOL, Material.GRASS_BLOCK, Material.DIRT)),
    OAK_SMALL("/trees/oak_small/s_oak_", 1, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT)),
    OAK_MEDIUM("/trees/oak_medium/m_oak_", 5, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT)),
    OAK_LARGE("/trees/oak_large/l_oak_", 2, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT)),
    JUNGLE_MEDIUM("/trees/jungle_medium/m_jungle_", 2, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT)),
    JUNGLE_LARGE("/trees/jungle_large/l_jungle_", 1, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT)),
    DEAD("/trees/dead/dead_", 4, Arrays.asList(Material.PODZOL, Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.SAND, Material.RED_SAND)),
    DARK_OAK_LARGE("/trees/dark_oak_large/l_darkoak_", 5,  Arrays.asList(Material.PODZOL, Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.SAND, Material.RED_SAND));

    private final String filePath;
    private final int permutations;
    private final List<Material> validSpawns;

    private final Map<String, Object> loadedStructures = new HashMap<>();
    TerraTree(String directory, int number, List<Material> validSpawns) {
        this.filePath = directory;
        this.permutations = number;
        this.validSpawns = validSpawns;
        for(int i = 0; i < number; i++) {
            Bukkit.getLogger().info("[Terra] Loading tree " + directory + i + " to memory.");
            loadedStructures.put(directory+i, NMSStructure.getAsTag(TerraTree.class.getResourceAsStream(directory + i + ".nbt")));
        }
    }

    private NMSStructure getInstance(Location origin, Random random) {
        return new NMSStructure(origin, loadedStructures.get(filePath + random.nextInt(permutations)));
    }

    @Override
    public void plant(Location location, Random random, boolean b, JavaPlugin javaPlugin) {
        if(!validSpawns.contains(location.clone().subtract(0, 1, 0).getBlock().getType())) return;
        NMSStructure temp = getInstance(location, random);
        int[] size = temp.getDimensions();
        switch(random.nextInt(4)) {
            case 0:
                temp.setOrigin(new Location(location.getWorld(), (int) (location.getBlockX()- (size[0]/2f)), location.getBlockY()-1, (int) (location.getBlockZ()- (size[2]/2f))));
                break;
            case 1:
                temp.setRotation(90);
                temp.setOrigin(new Location(location.getWorld(), (int) (location.getBlockX()+ (size[0]/2f)), location.getBlockY()-1, (int) (location.getBlockZ()- (size[2]/2f))));
                break;
            case 2:
                temp.setRotation(180);
                temp.setOrigin(new Location(location.getWorld(), (int) (location.getBlockX()+ (size[0]/2f)), location.getBlockY()-1, (int) (location.getBlockZ()+ (size[2]/2f))));
                break;
            case 3:
                temp.setRotation(270);
                temp.setOrigin(new Location(location.getWorld(), (int) (location.getBlockX()- (size[0]/2f)), location.getBlockY()-1, (int) (location.getBlockZ()+ (size[2]/2f))));
                break;
        }
        temp.paste();
        //location.getBlock().setType(Material.GOLD_BLOCK);
    }
}
