package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.mutable.MutableInteger;

import static org.junit.jupiter.api.Assertions.*;


public class ColumnTest {
    private final Column<Integer> returnY = new ColumnImpl<>(-10, 10, Function.identity());
    private final Column<Boolean> returnPositive = new ColumnImpl<>(-10, 10, i -> i >= 0);
    
    private final Column<Boolean> returnTrue = new ColumnImpl<>(-10, 10, i -> true);
    
    @Test
    public void testForEach() {
        returnY.forEach(Assertions::assertEquals);
        MutableInteger test = new MutableInteger(returnY.getMinY());
        returnY.forEach(i -> {
            assertEquals(i, test.get());
            test.increment();
        });
        assertEquals(returnY.getMaxY(), test.get());
    }
    
    @Test
    public void testForRanges() {
        List<Pair<Pair<Integer, Integer>, Boolean>> list = new ArrayList<>();
        
        returnPositive.forRanges(1, (min, max, p) -> list.add(Pair.of(Pair.of(min, max), p)));
        
        assertEquals(List.of(
                             Pair.of(Pair.of(-10, 0), false),
                             Pair.of(Pair.of(0, 10), true)
                            ),
                     list);
    }
    
    @Test
    public void testForRangesIndividual() {
        List<Pair<Pair<Integer, Integer>, Integer>> list = new ArrayList<>();
        
        returnY.forRanges(1, (min, max, p) -> list.add(Pair.of(Pair.of(min, max), p)));
        
        assertEquals(IntStream.range(-10, 10).mapToObj(i -> Pair.of(Pair.of(i, i + 1), i)).collect(Collectors.toList()),
                     list);
    }
    
    @Test
    public void testForRangesContiguous() {
        List<Pair<Pair<Integer, Integer>, Boolean>> list = new ArrayList<>();
        
        returnTrue.forRanges(1, (min, max, p) -> list.add(Pair.of(Pair.of(min, max), p)));
        
        assertEquals(List.of(Pair.of(Pair.of(-10, 10), true)),
                     list);
    }
    
    static class ColumnImpl<T> implements Column<T> {
        private final int min;
        private final int max;
        private final Function<Integer, T> p;
        
        ColumnImpl(int min, int max, Function<Integer, T> p) {
            this.min = min;
            this.max = max;
            this.p = p;
        }
        
        @Override
        public int getMinY() {
            return min;
        }
        
        @Override
        public int getMaxY() {
            return max;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getZ() {
            return 0;
        }

        @Override
        public T get(int y) {
            return p.apply(y);
        }
    }
}
