package com.dfsek.terra.api.structure.buffer.items;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;


public class Cell implements BufferedItem {
    private final List<BufferedItem> items = new ArrayList<>();
    private String mark;
    
    @Override
    public void paste(Vector3 origin, World world) {
        items.forEach(item -> item.paste(origin.clone(), world));
    }
    
    public void add(BufferedItem item) {
        items.add(item);
    }
    
    public String getMark() {
        return mark;
    }
    
    public void setMark(String mark) {
        this.mark = mark;
    }
}
