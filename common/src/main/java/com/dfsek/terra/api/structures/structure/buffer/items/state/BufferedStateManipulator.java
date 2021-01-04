package com.dfsek.terra.api.structures.structure.buffer.items.state;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;

@SuppressWarnings("unchecked")
public abstract class BufferedStateManipulator<T extends BlockState> implements BufferedItem {
    private final TerraPlugin main;

    protected BufferedStateManipulator(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void paste(Location origin) {
        BlockState state = origin.getBlock().getState();
        try {
            apply((T) state);
        } catch(ClassCastException e) {
            main.getLogger().warning("Could not find expected BlockState at " + origin + "; found " + origin.getBlock().getBlockData().getAsString());
        }
    }

    public abstract void apply(T state);
}
