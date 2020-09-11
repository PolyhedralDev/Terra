import com.dfsek.terra.biome.BiomeZone;
import org.polydev.gaea.math.FastNoise;

import java.util.Random;

public class DistributionTest {
    public static void main(String[] args) {
        FastNoise noise = new FastNoise(new Random().nextInt());
        noise.setFrequency(0.01f);
        noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        noise.setFractalOctaves(3);
        int[] numbers = new int[32];
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for(int i = 0; i < 32; i++) {
            numbers[i] = 0;
        }

        for(int i = 0; i < 10000000; i++) {
            double n = noise.getSimplexFractal(0, i);
            max = Math.max(max, n);
            min = Math.min(min, n);
            numbers[BiomeZone.normalize(n)]++;
        }

        for(int i = 0; i < 32; i++) {
            System.out.println(i + ": " + numbers[i]);
        }
        for(int i = 0; i < 32; i++) {
            System.out.print(i + " |");
            for(int j = 0; j < numbers[i]/2500; j++) {
                System.out.print("-");
            }
            System.out.println("|");
        }
        System.out.println("max: " + max);
        System.out.println("min: " + min);
    }
}
