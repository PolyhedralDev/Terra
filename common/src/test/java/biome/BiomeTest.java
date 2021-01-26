package biome;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.biome.StandardBiomeProvider;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.dfsek.terra.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.biome.pipeline.source.RandomSource;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.biome.pipeline.stages.SeededBuilder;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BiomeTest {
    @Test
    public static void main(String... args) {
        ProbabilityCollection<TerraBiome> oceanBiomes = new ProbabilityCollection<>();
        ProbabilityCollection<TerraBiome> landBiomes = new ProbabilityCollection<>();


        ProbabilityCollection<TerraBiome> beachBiomes = new ProbabilityCollection<>();

        TestBiome ocean = new TestBiome(Color.BLUE, "OCEAN_TEMP");
        TestBiome land = new TestBiome(Color.GREEN, "LAND_TEMP");

        TestBiome beach = new TestBiome(Color.YELLOW, "BEACH");
        beachBiomes.add(beach, 1);


        ProbabilityCollection<TerraBiome> climate = new ProbabilityCollection<>();
        climate.add(ocean, 1);
        climate.add(land, 2);


        oceanBiomes.add(new TestBiome(Color.BLUE, "OCEAN"), 10);
        oceanBiomes.add(new TestBiome(Color.CYAN, "OCEAN"), 1);

        landBiomes.add(new TestBiome(Color.GREEN, "LAND"), 8);
        landBiomes.add(new TestBiome(Color.ORANGE, "LAND"), 5);
        landBiomes.add(new TestBiome(Color.RED, "LAND"), 1);
        landBiomes.add(new TestBiome(Color.GRAY, "LAND"), 1);

        FastNoiseLite sourceSampler = new FastNoiseLite(123);
        sourceSampler.setNoiseType(FastNoiseLite.NoiseType.WhiteNoise);

        BiomeSource source = new RandomSource(climate, sourceSampler);


        BiomeProvider provider = new StandardBiomeProvider.StandardBiomeProviderBuilder((seed) -> new BiomePipeline.BiomePipelineBuilder(2)
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new ReplaceMutator("LAND_TEMP", landBiomes, whiteNoise(243)))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(2)))))
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new ReplaceMutator("OCEAN_TEMP", oceanBiomes, whiteNoise(243)))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(2)))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(2)))))
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new SmoothMutator(whiteNoise(3)))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(4)))))
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new SmoothMutator(whiteNoise(6)))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(4)))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(4)))))
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new BorderMutator("OCEAN", "LAND", whiteNoise(1234), beachBiomes))))
                .addStage(new SeededBuilder<>(s -> new ExpanderStage(new FractalExpander(whiteNoise(4)))))
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new SmoothMutator(whiteNoise(6)))))
                .addStage(new SeededBuilder<>(s -> new MutatorStage(new SmoothMutator(whiteNoise(6)))))
                .build(source, seed), null).build(0);


        int size = 1000;


        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);


        System.out.println(size);

        long s = System.nanoTime();
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                image.setRGB(x, z, provider.getBiome(x, z).getColor());
            }
        }
        long e = System.nanoTime();

        double time = e - s;
        time /= 1000000;
        System.out.println(time + "ms for " + size + "x" + size);

        JFrame frame = new JFrame("TerraBiome Viewer");


        frame.setResizable(false);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();


        System.out.println("Done");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

    }

    private static NoiseSampler whiteNoise(int seed) {
        FastNoiseLite noiseLite = new FastNoiseLite(seed);
        noiseLite.setNoiseType(FastNoiseLite.NoiseType.WhiteNoise);
        return noiseLite;
    }

    private final static class TestBiome implements TerraBiome {
        private final Color color;
        private final Set<String> tags;


        private TestBiome(Color color, String... tags) {
            this.color = color;
            this.tags = Arrays.stream(tags).collect(Collectors.toSet());
        }

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
            return color.getRGB();
        }

        @Override
        public Set<String> getTags() {
            return tags;
        }

        @Override
        public String getID() {
            return null;
        }
    }
}
