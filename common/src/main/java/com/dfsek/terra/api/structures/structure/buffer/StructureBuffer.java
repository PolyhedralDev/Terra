package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;
import com.dfsek.terra.api.structures.structure.buffer.items.Mark;
import net.jafama.FastMath;

import java.util.HashMap;
import java.util.Map;

public class StructureBuffer implements Buffer {
    private final Map<Vector3, Cell> bufferedItemMap = new HashMap<>();
    private final Location origin;
    private boolean succeeded;

    public StructureBuffer(Location origin) {
        this.origin = origin;
    }

    public void paste() {
        bufferedItemMap.forEach(((vector3, item) -> item.paste(origin.clone().add(vector3))));
    }

    public void paste(Chunk chunk) {
        bufferedItemMap.forEach(((vector3, item) -> {
            Location current = origin.clone().add(vector3);
            if(FastMath.floorDiv(current.getBlockX(), 16) != chunk.getX() || FastMath.floorDiv(current.getBlockZ(), 16) != chunk.getZ())
                return;
            item.paste(origin.clone().add(vector3));
        }));
    }

    @Override
    public Buffer addItem(BufferedItem item, Vector3 location) {
        bufferedItemMap.computeIfAbsent(location, l -> new Cell()).add(item);
        return this;
    }

    @Override
    public Mark getMark(Vector3 location) {
        Cell cell = bufferedItemMap.get(location);
        if(cell != null) {
            return cell.getMark();
        }
        return null;
    }

    @Override
    public Buffer setMark(Mark mark, Vector3 location) {
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
    public Location getOrigin() {
        return origin.clone();
    }
}
