import com.dfsek.terra.MaxMin;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxMinTest {
    @Test
    public void iterator() {
        MaxMin m = new MaxMin(0, 100);
        int i = 0;
        for(int mint : m) {
            assertEquals(i, mint);
            i++;
        }
        assertEquals(100, i);
    }
}
