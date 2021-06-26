package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.block.data.AnaloguePowerable;
import com.dfsek.terra.fabric.block.FabricBlockState;
import net.minecraft.block.BlockState;

/**
 * None of this actually has implementation, TODO: implement this if we ever end up needing it.
 */
public class FabricAnaloguePowerable extends FabricBlockState implements AnaloguePowerable {
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
