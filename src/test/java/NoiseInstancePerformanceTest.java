import org.junit.jupiter.api.Test;
import org.polydev.gaea.math.FastNoiseLite;

public class NoiseInstancePerformanceTest {
    @Test
    public void performance() {
        FastNoiseLite noiseLite = new FastNoiseLite(2403);
        long l = System.nanoTime();
        for(int i = 0; i < 10000000; i++) noiseLite.getNoise(i, i);
        System.out.println("No instantiation: " + (System.nanoTime() - l) / 1000000 + "ms");
        for(int i = 0; i < 10000000; i++) {
            FastNoiseLite noiseLite1 = new FastNoiseLite(2403);
            noiseLite1.getNoise(i, i);
        }
        System.out.println("Instantiation: " + (System.nanoTime() - l) / 1000000 + "ms");
    }
}
