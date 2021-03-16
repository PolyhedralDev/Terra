package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.block.state.Sign;
import org.jetbrains.annotations.NotNull;

public class BukkitSign extends BukkitBlockState implements Sign {
    protected BukkitSign(org.bukkit.block.Sign block) {
        super(block);
    }

    @Override
    public @NotNull String[] getLines() {
        return ((org.bukkit.block.Sign) getHandle()).getLines();
    }

    @Override
    public @NotNull String getLine(int index) throws IndexOutOfBoundsException {
        return ((org.bukkit.block.Sign) getHandle()).getLine(index);
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        ((org.bukkit.block.Sign) getHandle()).setLine(index, line);
    }

    @Override
    public void applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
