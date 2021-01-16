package biome;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.GenericLoaders;
import com.dfsek.terra.api.language.Language;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.config.files.FolderLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.config.biome.BiomeProviderBuilderLoader;
import com.dfsek.terra.config.templates.AbstractableTemplate;
import com.dfsek.terra.registry.ConfigRegistry;
import com.dfsek.terra.registry.TerraRegistry;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class DistributionTest {
    private static final TerraRegistry<TestBiome> BIOME_REGISTRY = new TerraRegistry<TestBiome>() {
    };
    private static final TerraPlugin DUMMY = new TerraPlugin() {
        @Override
        public WorldHandle getWorldHandle() {
            return null;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public TerraWorld getWorld(World world) {
            return null;
        }

        @Override
        public Logger getLogger() {
            return null;
        }

        @Override
        public PluginConfig getTerraConfig() {
            return null;
        }

        @Override
        public File getDataFolder() {
            return null;
        }

        @Override
        public boolean isDebug() {
            return false;
        }

        @Override
        public Language getLanguage() {
            return null;
        }

        @Override
        public ConfigRegistry getRegistry() {
            return null;
        }

        @Override
        public void reload() {

        }

        @Override
        public ItemHandle getItemHandle() {
            return null;
        }

        @Override
        public void saveDefaultConfig() {

        }

        @Override
        public String platformName() {
            return null;
        }

        @Override
        public void register(TypeRegistry registry) {
            registry.registerLoader(TerraBiome.class, BIOME_REGISTRY);
        }
    };

    @Test
    public static void main(String... args) throws ConfigException, IOException {
        JFrame testFrame = new JFrame("Biome Viewer");

        File pack = new File("/home/dfsek/Documents/Terra/platforms/bukkit/target/server/plugins/Terra/packs/default/");
        FolderLoader folderLoader = new FolderLoader(pack.toPath());

        AbstractConfigLoader loader = new AbstractConfigLoader();

        folderLoader.open("biomes", ".yml").then(inputStreams -> ConfigPack.buildAll((template, main) -> template, BIOME_REGISTRY, loader.load(inputStreams, TestBiome::new), null));

        BIOME_REGISTRY.forEach(System.out::println);
        BiomeProviderTemplate template = new BiomeProviderTemplate();
        ConfigLoader pipeLoader = new ConfigLoader()
                .registerLoader(BiomeProvider.BiomeProviderBuilder.class, new BiomeProviderBuilderLoader(null))
                .registerLoader(ProbabilityCollection.class, new ProbabilityCollectionLoader())
                .registerLoader(TerraBiome.class, BIOME_REGISTRY);
        new GenericLoaders(null).register(pipeLoader);

        pipeLoader.load(template, folderLoader.get("pack.yml"));
        BiomeProvider provider = template.getBiomeProviderBuilder().build(12);

        int size = 1024;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                image.setRGB(x, z, provider.getBiome(x, z).getColor());
            }
        }
        testFrame.add(new JLabel(new ImageIcon(image)));
        testFrame.pack();
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testFrame.setVisible(true);

    }

    private static final class BiomeProviderTemplate implements ConfigTemplate {
        @Value("biome-pipeline")
        BiomeProvider.BiomeProviderBuilder biomeProviderBuilder;

        public BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder() {
            return biomeProviderBuilder;
        }
    }

    private static final class TestBiome extends AbstractableTemplate implements TerraBiome, ValidatedConfigTemplate {

        @Value("color")
        @Default
        @Abstractable
        private int color;

        @Value("tags")
        @Abstractable
        @Default
        private Set<String> tags = new HashSet<>();

        @Value("id")
        private String id;

        @Override
        public ProbabilityCollection<Biome> getVanillaBiomes() {
            return null;
        }

        @Override
        public Generator getGenerator(World w) {
            return null;
        }

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public Set<String> getTags() {
            return tags;
        }

        @Override
        public boolean validate() {
            color += (255 << 24); // Alpha adjustment
            tags.add("BIOME:" + id);
            return true;
        }

        @Override
        public String getID() {
            return id;
        }

        @Override
        public String toString() {
            return id + ":" + color;
        }
    }
}
