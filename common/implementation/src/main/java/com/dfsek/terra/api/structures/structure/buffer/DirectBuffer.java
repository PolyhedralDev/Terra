package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.structure.buffer.BufferedItem;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Buffer implementation that directly pastes to the world.
 */
public class DirectBuffer implements Buffer {
    private final LocationImpl origin;
    private final Map<LocationImpl, String> marks = new LinkedHashMap<>();

    public DirectBuffer(LocationImpl origin) {
        this.origin = origin;
    }

    @Override
    public Buffer addItem(BufferedItem item, LocationImpl location) {
        item.paste(origin.clone().add(location));
        return this;
    }

    @Override
    public LocationImpl getOrigin() {
        return origin;
    }

    @Override
    public String getMark(LocationImpl location) {
        return marks.get(location);
    }

    @Override
    public Buffer setMark(String mark, LocationImpl location) {
        marks.put(location, mark);
        return this;
    }
}
