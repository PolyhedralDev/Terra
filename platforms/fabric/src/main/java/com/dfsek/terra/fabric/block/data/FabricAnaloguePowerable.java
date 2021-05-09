package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.platform.block.data.AnaloguePowerable;
import com.dfsek.terra.fabric.block.FabricBlockData;
import net.minecraft.block.BlockState;

/**
 * None of this actually has implementation, TODO: implement this if we ever end up needing it.
 */
public class FabricAnaloguePowerable extends FabricBlockData implements AnaloguePowerable {
    public FabricAnaloguePowerable(BlockState delegate) {
        super(delegate);
    }

    @Override
    public int getMaximumPower() {
        return 0;
    }

    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public void setPower(int power) {

    }
}
