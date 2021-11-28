/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.buffer.items;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;

import com.dfsek.terra.api.world.WritableWorld;

import org.jetbrains.annotations.ApiStatus.Experimental;


@Experimental
public class Cell implements BufferedItem {
    private final List<BufferedItem> items = new ArrayList<>();
    private String mark;
    
    @Override
    public void paste(Vector3 origin, WritableWorld world) {
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
