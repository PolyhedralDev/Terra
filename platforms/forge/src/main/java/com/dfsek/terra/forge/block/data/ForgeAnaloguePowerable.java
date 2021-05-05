package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.data.AnaloguePowerable;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.block.BlockState;

/**
 * None of this actually has implementation, TODO: implement this if we ever end up needing it.
 */
public class ForgeAnaloguePowerable extends ForgeBlockData implements AnaloguePowerable {
    public ForgeAnaloguePowerable(BlockState delegate) {
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
