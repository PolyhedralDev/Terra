package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.procgen.math.Vector2;
import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.StructureContainedBlock;
import com.dfsek.terra.structure.StructureInfo;
import com.dfsek.terra.util.structure.RotationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class TreeConfig extends TerraConfig implements Tree {
    private final Set<Material> spawnable;
    private final String id;
    private final int yOffset;
    private final ProbabilityCollection<Structure> structure = new ProbabilityCollection<>();

    public TreeConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        spawnable = ConfigUtil.toBlockData(getStringList("spawnable"), "spawnable", getID());
        if(!contains("id")) throw new ConfigException("No ID specified!", "null");
        id = getString("id");
        if(!contains("files")) throw new ConfigException("No files specified!", getID());
        yOffset = getInt("y-offset", 0);
        try {
            for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("files")).getValues(false).entrySet()) {
                try {
                    File structureFile = new File(config.getDataFolder() + File.separator + "trees" + File.separator + "data", e.getKey() + ".tstructure");
                    structure.add(Structure.load(structureFile), (Integer) e.getValue());
                } catch(FileNotFoundException ex) {
                    Debug.stack(ex);
                    throw new NotFoundException("Tree Structure File", e.getKey(), getID());
                } catch(ClassCastException ex) {
                    Debug.stack(ex);
                    throw new ConfigException("Unable to parse Tree configuration! Check YAML syntax.", getID());
                }
            }
        } catch(IOException | NullPointerException e) {
            if(ConfigUtil.debug) {
                e.printStackTrace();
            }
            throw new NotFoundException("Tree Structure", getString("file"), getID());
        }
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public boolean plant(Location location, Random random, JavaPlugin javaPlugin) {
        location.subtract(0, 1, 0);
        Location mut = location.clone().subtract(0, yOffset, 0);
        if(!spawnable.contains(location.getBlock().getType())) return false;
        Structure struc = structure.get(random);
        Rotation rotation = Rotation.fromDegrees(random.nextInt(4) * 90);
        if(!struc.checkSpawns(mut, rotation)) return false;
        struc.paste(mut, rotation);
        return true;
    }

    public boolean plantBlockCheck(Location location, Random random) {
        location.subtract(0, 1, 0);
        Location mut = location.clone().subtract(0, yOffset, 0);
        if(!spawnable.contains(location.getBlock().getType())) return false;
        Structure struc = structure.get(random);
        Rotation rotation = Rotation.fromDegrees(random.nextInt(4) * 90);
        StructureInfo info = struc.getStructureInfo();
        for(StructureContainedBlock spawn : struc.getSpawns()) {
            Vector2 rot = RotationUtil.getRotatedCoords(new Vector2(spawn.getX() - info.getCenterX(), spawn.getZ() - info.getCenterZ()), rotation);
            int x = (int) rot.getX();
            int z = (int) rot.getZ();
            switch(spawn.getRequirement()) {
                case AIR:
                    if(!mut.clone().add(x, spawn.getY() - 1, z).getBlock().isPassable()) return false;
                    break;
                case LAND:
                    if(!mut.clone().add(x, spawn.getY() - 1, z).getBlock().getType().isSolid()) return false;
                    break;
            }
        }
        struc.paste(mut, rotation);
        return true;
    }
}
