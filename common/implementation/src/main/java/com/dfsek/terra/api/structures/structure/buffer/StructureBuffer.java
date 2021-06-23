package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import net.jafama.FastMath;

import java.util.LinkedHashMap;
import java.util.Map;

public class StructureBuffer implements Buffer {
    private final Map<LocationImpl, Cell> bufferedItemMap = new LinkedHashMap<>();
    private final LocationImpl origin;
    private boolean succeeded;

    public StructureBuffer(LocationImpl origin) {
        this.origin = origin;
    }

    public void paste() {
        bufferedItemMap.forEach(((vector3, item) -> item.paste(origin.clone().add(vector3))));
    }

    public void paste(Chunk chunk) {
        bufferedItemMap.forEach(((location, item) -> {
            LocationImpl current = origin.clone().add(location);
            if(FastMath.floorDiv(current.getBlockX(), 16) != chunk.getX() || FastMath.floorDiv(current.getBlockZ(), 16) != chunk.getZ())
                return;
            item.paste(chunk, current);
        }));
    }

    @Override
    public Buffer addItem(BufferedItem item, LocationImpl location) {
        bufferedItemMap.computeIfAbsent(location, l -> new Cell()).add(item);
        return this;
    }

    @Override
    public String getMark(LocationImpl location) {
        Cell cell = bufferedItemMap.get(location);
        if(cell != null) {
            return cell.getMark();
        }
        return null;
    }

    @Override
    public Buffer setMark(String mark, LocationImpl location) {
        bufferedItemMap.computeIfAbsent(location, l -> new Cell()).setMark(mark);
        return this;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public boolean succeeded() {
        return succeeded;
    }

    @Override
    public LocationImpl getOrigin() {
        return origin.clone();
    }
}
