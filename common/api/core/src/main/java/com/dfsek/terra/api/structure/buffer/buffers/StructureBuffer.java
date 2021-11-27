/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.buffer.buffers;

import net.jafama.FastMath;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.structure.buffer.items.Cell;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.World;

import org.jetbrains.annotations.ApiStatus.Experimental;


@Experimental
public class StructureBuffer implements Buffer {
    private final Map<Vector3, Cell> bufferedItemMap = new LinkedHashMap<>();
    private final Vector3 origin;
    private boolean succeeded;
    
    public StructureBuffer(Vector3 origin) {
        this.origin = origin;
    }
    
    public void paste(Vector3 origin, Chunk chunk) {
        bufferedItemMap.forEach(((location, item) -> {
            Vector3 current = origin.clone().add(location);
            if(FastMath.floorDiv(current.getBlockX(), 16) != chunk.getX() || FastMath.floorDiv(current.getBlockZ(), 16) != chunk.getZ())
                return;
            item.paste(current, chunk.getWorld());
        }));
    }
    
    public void paste(Vector3 origin, World world) {
        bufferedItemMap.forEach(((vector3, item) -> item.paste(origin.clone().add(vector3), world)));
    }
    
    @Override
    public Buffer addItem(BufferedItem item, Vector3 location) {
        bufferedItemMap.computeIfAbsent(location.clone(), l -> new Cell()).add(item);
        return this;
    }
    
    @Override
    public Buffer setMark(String mark, Vector3 location) {
        bufferedItemMap.computeIfAbsent(location.clone(), l -> new Cell()).setMark(mark);
        return this;
    }
    
    @Override
    public Vector3 getOrigin() {
        return origin.clone();
    }
    
    @Override
    public String getMark(Vector3 location) {
        Cell cell = bufferedItemMap.get(location);
        if(cell != null) {
            return cell.getMark();
        }
        return null;
    }
    
    public boolean succeeded() {
        return succeeded;
    }
    
    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }
}
