package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.GlueList;

import java.util.List;

public class Cell implements BufferedItem {
    private final List<BufferedItem> items = new GlueList<>();
    private String mark = null;


    @Override
    public void paste(Location origin) {
        items.forEach(item -> item.paste(origin));
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
