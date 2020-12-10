import com.dfsek.terra.api.gaea.math.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RangeTest {
    @Test
    public void iterator() {
        Range m = new Range(0, 100);
        int i = 0;
        for(int mint : m) {
            assertEquals(i, mint);
            i++;
        }
        assertEquals(100, i);
    }

    @Test
    public void intersect() {
        Range one = new Range(10, 100);
        Range two = new Range(1, 20);
        Range intersect = one.intersects(two);
        assertEquals(20, intersect.getMax());
        assertEquals(10, intersect.getMin());
        assertEquals(one.intersects(two), two.intersects(one));

        one = new Range(25, 50);
        assertNull(one.intersects(two));
    }

    @Test
    public void reflect() {
        Range t = new Range(3, 10);
        Range other = t.reflect(5);
        assertEquals(7, other.getMax());
        assertEquals(0, other.getMin());
    }
}
