package com.dfsek.terra.registry.master;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.config.factories.BiomeFactory;
import com.dfsek.terra.config.factories.CarverFactory;
import com.dfsek.terra.config.factories.ConfigFactory;
import com.dfsek.terra.config.factories.FloraFactory;
import com.dfsek.terra.config.factories.OreFactory;
import com.dfsek.terra.config.factories.PaletteFactory;
import com.dfsek.terra.config.factories.StructureFactory;
import com.dfsek.terra.config.factories.TreeFactory;
import com.dfsek.terra.config.prototype.ConfigType;
import com.dfsek.terra.config.templates.AbstractableTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.config.templates.TreeTemplate;
import com.dfsek.terra.registry.OpenRegistry;

public class ConfigTypeRegistry extends OpenRegistry<ConfigType<?>> {
    public ConfigTypeRegistry() {
        add("BIOME", (pack, main) -> load(pack.getBiomeRegistry(), new BiomeTemplate(pack, main), new BiomeFactory(pack), main));
        add("PALETTE", (pack, main) -> load(pack.getPaletteRegistry(), new PaletteTemplate(), new PaletteFactory(), main));
        add("ORE", (pack, main) -> load(pack.getOreRegistry(), new OreTemplate(), new OreFactory(), main));
        add("FLORA", (pack, main) -> load(pack.getFloraRegistry(), new FloraTemplate(), new FloraFactory(), main));
        add("CARVER", (pack, main) -> load(pack.getCarverRegistry(), new CarverTemplate(), new CarverFactory(pack), main));
        add("STRUCTURE", (pack, main) -> load(pack.getStructureRegistry(), new StructureTemplate(), new StructureFactory(), main));
        add("TREE", (pack, main) -> load(pack.getTreeRegistry(), new TreeTemplate(), new TreeFactory(), main));
    }

    @SuppressWarnings("deprecation")
    private <T extends AbstractableTemplate, O> T load(CheckedRegistry<O> registry, T object, ConfigFactory<T, O> factory, TerraPlugin main) throws LoadException {
        registry.addUnchecked(object.getID(), factory.build(object, main));
        return object;
    }
}
