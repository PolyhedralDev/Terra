package com.dfsek.terra.registry.config;

import com.dfsek.tectonic.config.ConfigTemplate;
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
import com.dfsek.terra.config.pack.ConfigPack;
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

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigTypeRegistry extends OpenRegistry<ConfigType<?>> {
    public ConfigTypeRegistry(ConfigPack pack, TerraPlugin main) {
        super(new LinkedHashMap<>()); // Ordered
        add("PALETTE", new ConfigBuilder<>(pack.getPaletteRegistry(), new PaletteFactory(), PaletteTemplate::new));
        add("ORE", new ConfigBuilder<>(pack.getOreRegistry(), new OreFactory(), OreTemplate::new));
        add("FLORA", new ConfigBuilder<>(pack.getFloraRegistry(), new FloraFactory(), FloraTemplate::new));
        add("CARVER", new ConfigBuilder<>(pack.getCarverRegistry(), new CarverFactory(pack), CarverTemplate::new));
        add("STRUCTURE", new ConfigBuilder<>(pack.getStructureRegistry(), new StructureFactory(), StructureTemplate::new));
        add("TREE", new ConfigBuilder<>(pack.getTreeRegistry(), new TreeFactory(), TreeTemplate::new));
        add("BIOME", new ConfigBuilder<>(pack.getBiomeRegistry(), new BiomeFactory(pack), () -> new BiomeTemplate(pack, main)));
        add("PACK", new PackBuilder());
    }

    private static final class PackBuilder implements ConfigType<ConfigTemplate> {

        @Override
        public ConfigTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
            return new ConfigTemplate() {
            };
        }

        @Override
        public void callback(ConfigPack pack, TerraPlugin main, ConfigTemplate loadedConfig) {

        }
    }

    private static final class ConfigBuilder<T extends AbstractableTemplate, O> implements ConfigType<T> {
        private final CheckedRegistry<O> registry;
        private final ConfigFactory<T, O> factory;
        private final Supplier<T> provider;

        private ConfigBuilder(CheckedRegistry<O> registry, ConfigFactory<T, O> factory, Supplier<T> provider) {
            this.registry = registry;
            this.factory = factory;
            this.provider = provider;
        }

        @Override
        public T getTemplate(ConfigPack pack, TerraPlugin main) {
            return provider.get();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void callback(ConfigPack pack, TerraPlugin main, T loadedConfig) throws LoadException {
            registry.addUnchecked(loadedConfig.getID(), factory.build(loadedConfig, main));
        }
    }
}
