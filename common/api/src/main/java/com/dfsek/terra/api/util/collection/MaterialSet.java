/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.collection;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;


public class MaterialSet extends HashSet<BlockType> {
    @Serial
    private static final long serialVersionUID = 3056512763631017301L;
    
    public static MaterialSet singleton(BlockType material) {
        return new Singleton(material);
    }
    
    public static MaterialSet get(BlockType... materials) {
        MaterialSet set = new MaterialSet();
        set.addAll(Arrays.asList(materials));
        return set;
    }
    
    public static MaterialSet get(BlockState... materials) {
        MaterialSet set = new MaterialSet();
        Arrays.stream(materials).forEach(set::add);
        return set;
    }
    
    public static MaterialSet empty() {
        return new MaterialSet();
    }
    
    private void add(BlockState data) {
        add(data.getBlockType());
    }
    
    private static final class Singleton extends MaterialSet {
        private final BlockType element;
        
        Singleton(BlockType e) {
            element = e;
        }
        
        public Iterator<BlockType> iterator() {
            return new Iterator<>() {
                private boolean hasNext = true;
                
                public boolean hasNext() {
                    return hasNext;
                }
                
                public BlockType next() {
                    if(hasNext) {
                        hasNext = false;
                        return element;
                    }
                    throw new NoSuchElementException();
                }
                
                public void remove() {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void forEachRemaining(Consumer<? super BlockType> action) {
                    Objects.requireNonNull(action);
                    if(hasNext) {
                        hasNext = false;
                        action.accept(element);
                    }
                }
            };
        }
        
        public int size() {
            return 1;
        }
        
        public boolean contains(Object o) {
            return Objects.equals(o, element);
        }
        
        // Override default methods for Collection
        @Override
        public void forEach(Consumer<? super BlockType> action) {
            action.accept(element);
        }
        
        @Override
        public Spliterator<BlockType> spliterator() {
            return new Spliterator<>() {
                long est = 1;
                
                @Override
                public Spliterator<BlockType> trySplit() {
                    return null;
                }
                
                @Override
                public boolean tryAdvance(Consumer<? super BlockType> consumer) {
                    Objects.requireNonNull(consumer);
                    if(est > 0) {
                        est--;
                        consumer.accept(element);
                        return true;
                    }
                    return false;
                }
                
                @Override
                public void forEachRemaining(Consumer<? super BlockType> consumer) {
                    tryAdvance(consumer);
                }
                
                @Override
                public long estimateSize() {
                    return est;
                }
                
                @Override
                public int characteristics() {
                    int value = (element != null) ? Spliterator.NONNULL : 0;
                    
                    return value | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE |
                           Spliterator.DISTINCT | Spliterator.ORDERED;
                }
            };
        }
        
        @Override
        public boolean removeIf(Predicate<? super BlockType> filter) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(element);
        }
    }
}
