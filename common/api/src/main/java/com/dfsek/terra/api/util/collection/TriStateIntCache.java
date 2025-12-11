package com.dfsek.terra.api.util.collection;

import java.util.concurrent.atomic.AtomicLongArray;

public class TriStateIntCache {
    public static final long STATE_UNSET = 0L;
    public static final long STATE_FALSE = 1L;
    public static final long STATE_TRUE  = 2L;

    private static final long BIT_MASK = 3L;
    private final AtomicLongArray data;

    public TriStateIntCache(int maxKeySize) {
        this.data = new AtomicLongArray((maxKeySize + 31) >>> 5);
    }

    /**
     * Checks the cache state without any allocation.
     * @return STATE_UNSET (0), STATE_FALSE (1), or STATE_TRUE (2)
     */
    public long get(int key) {
        int arrayIndex = key >>> 5;
        int bitShift = (key & 31) << 1;
        long currentWord = data.get(arrayIndex);
        return (currentWord >>> bitShift) & BIT_MASK;
    }

    /**
     * Sets the value safely. Handles race conditions internally.
     */
    public void set(int key, boolean value) {
        int arrayIndex = key >>> 5;
        int bitShift = (key & 31) << 1;
        long targetState = value ? STATE_TRUE : STATE_FALSE;

        long currentWord, newWord;
        do {
            currentWord = data.get(arrayIndex);

            // Race condition check:
            long existingState = (currentWord >>> bitShift) & BIT_MASK;
            if (existingState != STATE_UNSET) {
                return; // Already set, abort our update
            }

            // Create new word with our bit set
            newWord = (currentWord & ~(BIT_MASK << bitShift)) | (targetState << bitShift);

        } while (!data.compareAndSet(arrayIndex, currentWord, newWord));
    }
}