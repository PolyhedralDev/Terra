import com.dfsek.terra.biome.BiomeZone;
import org.polydev.gaea.math.FastNoise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LookupGenerator {
    private static double[] lookup;

    public static void main(String[] args) {
        int dist = 16;

        List<Double> vals = new ArrayList<>();
        FastNoise noise = new FastNoise(new Random().nextInt());
        noise.setFrequency(0.01f);
        noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        noise.setFractalOctaves(5);
        int[] numbers = new int[dist];
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for(int i = 0; i < dist; i++) {
            numbers[i] = 0;
        }

        for(int i = 0; i < 10000000; i++) {
            double n = noise.getNoise(0, i);
            max = Math.max(max, n);
            min = Math.min(min, n);
            vals.add(n);
            numbers[normalize(n, dist)]++;
        }

        for(int i = 0; i < dist; i++) {
            System.out.println(i + ": " + numbers[i]);
        }
        for(int i = 0; i < dist; i++) {
            System.out.print(i + (String.valueOf(i).length() ==1 ? " " : "") + " |");
            for(int j = 0; j < numbers[i]/3000; j++) {
                System.out.print("-");
            }
            System.out.println("|");
        }
        System.out.println("max: " + max);
        System.out.println("min: " + min);
        Collections.sort(vals);

        lookup = new double[dist];
        StringBuilder s = new StringBuilder("{");
        for(int i = 0; i < dist; i++) {
            int current = vals.size()/dist;
            System.out.println(i + ", max: " + vals.get(current*(i+1)-1));
            lookup[i] = vals.get(current*(i+1)-1);
            s.append(vals.get(current*(i+1)-1) + "D, ");
        }
        s.delete(s.length()-2, s.length());
        s.append("}");
        numbers = new int[dist];
        vals = new ArrayList<>();
        for(int i = 0; i < 10000000; i++) {
            double n = noise.getNoise(0, i);
            vals.add(n);
            numbers[normalizeNew(n)]++;
        }

        for(int i = 0; i < dist; i++) {
            System.out.println(i + ": " + numbers[i]);
        }
        for(int i = 0; i < dist; i++) {
            System.out.print(i + (String.valueOf(i).length() ==1 ? " " : "") + " |");
            for(int j = 0; j < numbers[i]/3000; j++) {
                System.out.print("-");
            }
            System.out.println("|");
        }
        System.out.println(s.toString());

    }
    public static int normalize(double i, int n) {
        i*=1.42; // Magic simplex value (sqrt(2) plus a little)
        i = Math.min(Math.max(i, -1),1);
        return Math.min((int) Math.floor((i+1)*((double)n/2)), n-1);
    }

    public static int normalizeNew(double d) {
        for(int i = 0; i < lookup.length; i++) {
            if(d < lookup[i]) return i;
        }
        return lookup.length-1;
    }
}
