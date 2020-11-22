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
        int a = 0;
        for(int i = 0; i < 200; i++) {
            long l = System.nanoTime();
            for(int j = 0; j < 5000; j++) {
                for(int x = 0; x < 4; x++) {
                    for(int y = 0; y < 64; y++) {
                        for(int z = 0; z < 4; z++) {
                            noiseFunction.eval(Arrays.asList(get(j * 16 + (x * 4)), get(i * 16 + (z * 4))));
                        }
                    }
                }
            }
            double n = System.nanoTime() - l;
            System.out.print((long) n / 1000000 + "ms" + ((a % 10 == 0) ? "\n" : " "));
            a++;
        }
        System.out.println("No Cache:");
        for(int i = 0; i < 200; i++) {
            long l = System.nanoTime();
            for(int j = 0; j < 5000; j++) {
                for(int x = 0; x < 4; x++) {
                    for(int y = 0; y < 64; y++) {
                        for(int z = 0; z < 4; z++) {
                            noiseFunction.evalNoCache(Arrays.asList(get(j * 16 + (x * 4)), get(i * 16 + (z * 4))));
                        }
                    }
                }
            }
            double n = System.nanoTime() - l;
            System.out.print((long) n / 1000000 + "ms" + ((a % 10 == 0) ? "\n" : " "));
            a++;
        }
    }

    private Expression get(double val) {
        return () -> val;
    }
}
