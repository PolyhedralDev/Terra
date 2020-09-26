package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.StructureSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.population.GaeaBlockPopulator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StructurePopulator extends GaeaBlockPopulator {
    StructureSpawn spawnTest = new StructureSpawn(100, 5);
    Set<Chunk> pop = new HashSet<>();
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if(pop.contains(chunk)) Bukkit.getLogger().warning("Already populated structures in chunk: " + chunk);
        pop.add(chunk);
        Location near = spawnTest.getNearestSpawn((chunk.getX() << 4) + 8,  (chunk.getZ() << 4), world.getSeed()).toLocation(world);
        if(near.getChunk().equals(chunk)) {
            Terra.getInstance().getLogger().info("Spawning structure at " + near.toString() + " in chunk " + chunk);
            try {
                GaeaStructure struc = GaeaStructure.load(new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", "demo.tstructure"));
                near.setY(world.getHighestBlockYAt(near));
                struc.paste(near);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
