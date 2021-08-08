package com.dfsek.terra.addons.terrascript.api.buffer;

import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

import java.util.ArrayList;
import java.util.List;

public class Cell implements BufferedItem {
    private final List<BufferedItem> items = new ArrayList<>();
    private String mark = null;

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
