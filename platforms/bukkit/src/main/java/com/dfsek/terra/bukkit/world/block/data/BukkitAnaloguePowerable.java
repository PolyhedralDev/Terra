package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.data.AnaloguePowerable;

public class BukkitAnaloguePowerable extends BukkitBlockData implements AnaloguePowerable {
    public BukkitAnaloguePowerable(org.bukkit.block.data.AnaloguePowerable delegate) {
        super(delegate);
    }

    @Override
    public int getMaximumPower() {
        return ((org.bukkit.block.data.AnaloguePowerable) getHandle()).getMaximumPower();
    }

    @Override
    public int getPower() {
        return ((org.bukkit.block.data.AnaloguePowerable) getHandle()).getPower();
    }

    @Override
    public void setPower(int power) {
        ((org.bukkit.block.data.AnaloguePowerable) getHandle()).setPower(power);
    }
}
