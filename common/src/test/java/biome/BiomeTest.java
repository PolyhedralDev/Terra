package biome;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.dfsek.terra.biome.pipeline.TerraBiomeHolder;
import com.dfsek.terra.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.biome.pipeline.source.RandomSource;
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
        climate.add(land, 3);


        oceanBiomes.add(new TestBiome(Color.BLUE, "OCEAN"), 10);
        oceanBiomes.add(new TestBiome(Color.CYAN, "OCEAN"), 1);

        landBiomes.add(new TestBiome(Color.GREEN, "LAND"), 20);
        landBiomes.add(new TestBiome(Color.ORANGE, "LAND"), 5);
        landBiomes.add(new TestBiome(Color.RED, "LAND"), 1);
        landBiomes.add(new TestBiome(Color.GRAY, "LAND"), 1);

        FastNoiseLite sourceSampler = new FastNoiseLite(123);
        sourceSampler.setNoiseType(FastNoiseLite.NoiseType.WhiteNoise);

        BiomeSource source = new RandomSource(climate, sourceSampler);

        int size = 25;
        int expand = 6;

        BiomeHolder holder = new TerraBiomeHolder(size, new Vector2(0, 0));
        BiomeHolder holder2 = new TerraBiomeHolder(size, new Vector2(24, 0));
        BiomeHolder holder3 = new TerraBiomeHolder(size, new Vector2(0, 24));
        BiomeHolder holder4 = new TerraBiomeHolder(size, new Vector2(24, 24));

        long s = System.nanoTime();
        holder.fill(source);
        holder2.fill(source);
        holder3.fill(source);
        holder4.fill(source);

        for(int i = 0; i < 5; i++) {
            holder = holder.expand(new FractalExpander(whiteNoise(i)));
            holder2 = holder2.expand(new FractalExpander(whiteNoise(i)));
            holder3 = holder3.expand(new FractalExpander(whiteNoise(i)));
            holder4 = holder4.expand(new FractalExpander(whiteNoise(i)));
            size = size * 2 - 1;
        }
        int og = size;
        size *= 2;
        //holder = holder.expand(new FractalExpander(whiteNoise(4)));

        //holder.mutate(new ReplaceMutator("OCEAN_TEMP", oceanBiomes, whiteNoise(234)));
        //holder.mutate(new ReplaceMutator("LAND_TEMP", landBiomes, whiteNoise(235)));

        //holder = holder.expand(new FractalExpander(whiteNoise(3)));
        //holder = holder.expand(new FractalExpander(whiteNoise(2)));

        //holder.mutate(new SmoothMutator(whiteNoise(34)));

        //holder = holder.expand(new FractalExpander(whiteNoise(5)));
        //holder = holder.expand(new FractalExpander(whiteNoise(7)));

        //holder.mutate(new BorderMutator(Sets.newHashSet("OCEAN"), "LAND", whiteNoise(2356), beachBiomes));

        //holder = holder.expand(new FractalExpander(whiteNoise(6)));

        //holder.mutate(new SmoothMutator(whiteNoise(35)));

        long e = System.nanoTime();

        double time = e - s;
        time /= 1000000;
        System.out.println(time + "ms for " + size + "x" + size);


        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);


        System.out.println(size);

        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                if(x < og) {
                    if(z < og) image.setRGB(x, z, holder.getBiome(x, z).getColor());
                    else image.setRGB(x, z, holder3.getBiome(x, z - og).getColor());
                } else {
                    if(z < og) image.setRGB(x, z, holder2.getBiome(x - og, z).getColor());
                    else image.setRGB(x, z, holder4.getBiome(x - og, z - og).getColor());
                }
            }
        }

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
        public com.dfsek.terra.api.platform.world.Biome getVanillaBiome() {
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
    }
}
