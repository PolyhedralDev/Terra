import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.math.NoiseFunction2;
import org.junit.jupiter.api.Test;
import parsii.eval.Expression;

import java.util.Arrays;

public class NoiseTest {
    @Test
    public void noise() {
        NoiseFunction2 noiseFunction = new NoiseFunction2(12345, new NoiseBuilder());
        System.out.println("Cache:");
        for(int i = 0; i < 10; i++) {
            long l = System.nanoTime();
            for(int j = 0; j < 20000; j++) {
                for(int x = 0; x < 4; x++) {
                    for(int y = 0; y < 64; y++) {
                        for(int z = 0; z < 4; z++) {
                            noiseFunction.eval(Arrays.asList(get(j * 16 + (x * 4)), get(i * 16 + (z * 4))));
                        }
                    }
                }
            }
            double n = System.nanoTime() - l;
            System.out.println(n / 1000000 + "ms");
        }
        System.out.println("No Cache:");
        for(int i = 0; i < 10; i++) {
            long l = System.nanoTime();
            for(int j = 0; j < 20000; j++) {
                for(int x = 0; x < 4; x++) {
                    for(int y = 0; y < 64; y++) {
                        for(int z = 0; z < 4; z++) {
                            noiseFunction.evalNoCache(Arrays.asList(get(j * 16 + (x * 4)), get(i * 16 + (z * 4))));
                        }
                    }
                }
            }
            double n = System.nanoTime() - l;
            System.out.println(n / 1000000 + "ms");
        }
    }

    private Expression get(double val) {
        return () -> val;
    }
}
