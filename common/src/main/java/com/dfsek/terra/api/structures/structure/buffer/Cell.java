package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;
import com.dfsek.terra.api.structures.structure.buffer.items.Mark;
import com.dfsek.terra.api.util.GlueList;

import java.util.List;

public class Cell implements BufferedItem {
    private final List<BufferedItem> items = new GlueList<>();
    private Mark mark = null;


    @Override
    public void paste(Location origin) {
        items.forEach(item -> item.paste(origin));
    }

    public void add(BufferedItem item) {
        items.add(item);
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }
}
