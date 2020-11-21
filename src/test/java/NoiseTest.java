import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.math.NoiseFunction2;
import org.junit.jupiter.api.Test;
import parsii.eval.Expression;

import java.util.Arrays;

public class NoiseTest {
    @Test
    public void noise() {
        NoiseFunction2 noiseFunction = new NoiseFunction2(12345, new NoiseBuilder());

        for(int i = 0; i < 10; i++) {
            long l = System.nanoTime();
            for(int j = 0; j < 1000000; j++) {
                noiseFunction.eval(Arrays.asList(get(j), get(i)));
                noiseFunction.eval(Arrays.asList(get(j), get(i)));
                noiseFunction.eval(Arrays.asList(get(j), get(i)));
            }
            double n = System.nanoTime() - l;
            System.out.println(n / 1000000 + "ms");
        }
    }

    private Expression get(double val) {
        return () -> val;
    }
}
