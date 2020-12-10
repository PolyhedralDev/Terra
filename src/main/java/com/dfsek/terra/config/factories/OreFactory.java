package com.dfsek.terra.config.factories;

import com.dfsek.terra.Terra;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.generation.items.ores.DeformedSphereOre;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.generation.items.ores.VanillaOre;
import org.bukkit.block.data.BlockData;

public class OreFactory implements TerraFactory<OreTemplate, Ore> {
    @Override
    public Ore build(OreTemplate config, Terra main) {
        BlockData m = config.getMaterial();
        switch(config.getType()) {
            case SPHERE:
                return new DeformedSphereOre(m, config.getReplaceable(), config.doPhysics(), config.getDeform(), config.getDeformFrequency(), config.getSize(), main);
            case VANILLA:
                return new VanillaOre(m, config.getReplaceable(), config.doPhysics(), config.getSize(), main);
        }
        return null;
    }
}
