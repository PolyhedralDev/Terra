import com.dfsek.terra.biome.BiomeZone;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.polydev.gaea.math.FastNoise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LookupGenerator {
    private static double[] lookup;

    public static void main(String[] args) throws InterruptedException {
        int dist = 4096;

        List<Double> vals = new ArrayList<>();
        FastNoise noise = new FastNoise();
        noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        noise.setFrequency(0.02f);
        noise.setFractalOctaves(4);
        int[] numbers = new int[dist];
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for(int i = 0; i < dist; i++) {
            numbers[i] = 0;
        }


        int workerAmount = 16;

        List<Worker> workers = new ArrayList<>();

        for(int i = 0; i < workerAmount; i++) {
            workers.add(new Worker(new ArrayList<>(), 10000000, noise));
        }

        for(Worker w : workers) {
            w.start();
        }
        for(Worker w : workers) {
            System.out.println("Waiting for Worker " + workers.indexOf(w));
            w.join();
        }

        for(Worker w : workers) {
            vals.addAll(w.getResult());
        }

        System.out.println("Generated " + vals.size() + " values.");

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
        for(int i = 0; i < 50000000; i++) {
            double n = noise.getNoise(0, i);
            vals.add(n);
            numbers[normalizeNew(n)]++;
        }

        for(int i = 0; i < dist; i++) {
            System.out.println(i + ": " + numbers[i]);
        }
        for(int i = 0; i < dist; i++) {
            System.out.print(i + (String.valueOf(i).length() ==1 ? " " : "") + " |");
            for(int j = 0; j < numbers[i]/100; j++) {
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

    private static class Worker extends Thread {
        private final List<Double> l;
        private final int searches;
        private final FastNoise noise;
        public Worker(List<Double> l, int searches, FastNoise noise) {
            this.l = l;
            this.searches = searches;
            this.noise = noise;
        }

        @Override
        public void run() {
            for(int i = 0; i < searches; i++) {
                double n = noise.getNoise(0, i);
                l.add(n);
            }
        }

        public List<Double> getResult() {
            return l;
        }

        public String getStatus() {
            return "Generating values. " + l.size() + "/" + searches + " (" + ((long)l.size()*100L)/searches + "%)";
        }
    }
}
