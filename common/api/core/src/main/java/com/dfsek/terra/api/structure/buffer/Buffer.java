package com.dfsek.terra.api.structure.buffer;

import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;


public interface Buffer {
    void paste(Vector3 origin, Chunk chunk);
    
    void paste(Vector3 origin, World world);
    
    Buffer addItem(BufferedItem item, Vector3 location);
    
    Buffer setMark(String mark, Vector3 location);
    
    Vector3 getOrigin();
    
    String getMark(Vector3 location);
}
