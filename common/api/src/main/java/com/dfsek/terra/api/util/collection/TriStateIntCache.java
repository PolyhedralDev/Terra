package com.dfsek.terra.api.util.collection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

import com.dfsek.seismic.util.UnsafeUtils;
import sun.misc.Unsafe;

public class TriStateIntCache {
    public static final long STATE_UNSET = 0L;
    public static final long STATE_FALSE = 1L;
    public static final long STATE_TRUE = 2L;

    private static final long BIT_MASK = 3L;
    private final long[] data;

    private static final VarHandle ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(long[].class);

    private static final long ARRAY_BASE_OFFSET;

    static {
        assert UnsafeUtils.UNSAFE != null;
        ARRAY_BASE_OFFSET = UnsafeUtils.UNSAFE.arrayBaseOffset(long[].class);
    }

    private static int getOptimalMaxKeys(int requestedKeys) {
        // 192 keys fill the first cache line exactly (along with the 16-byte header)
        if (requestedKeys <= 192) {
            return 192;
        }

        // For every additional line, we fit 256 keys (64 bytes * 4 keys/byte)
        // We calculate the overflow beyond 192, round up to the nearest 256, and add it back.
        int overflow = requestedKeys - 192;
        int chunks = (overflow + 255) >>> 8; // Fast ceil division by 256

        return 192 + (chunks << 8); // chunks * 256
    }

    public TriStateIntCache(int maxKeySize) {
        this.data = new long[(getOptimalMaxKeys(maxKeySize) + 31) >>> 5];
    }

    /**
     * Checks the cache state without any allocation.
     *
     * @return STATE_UNSET (0), STATE_FALSE (1), or STATE_TRUE (2)
     */
    public long get(int key) {
        long offset = ARRAY_BASE_OFFSET + ((long)(key >>> 5) << 3);
        long currentWord = UnsafeUtils.UNSAFE.getLong(data, offset);
        return (currentWord >>> ((key << 1) & 63)) & BIT_MASK;
    }

    /**
     * Sets the value safely. Handles race conditions internally.
     */
    public void set(int key, boolean value) {
        int index = key >>> 5;
        int shift = (key << 1) & 63;

        long targetWord = (value ? STATE_TRUE : STATE_FALSE) << shift;

        long current;
        do {
            current = (long) ARRAY_HANDLE.getVolatile(data, index);

            if (((current >>> shift) & BIT_MASK) != STATE_UNSET) {
                return;
            }

        } while (!ARRAY_HANDLE.compareAndSet(data, index, current, current | targetWord));
    }
}
