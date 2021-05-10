package com.dfsek.terra.forge.world.block.state;

import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.block.state.Sign;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;

public class ForgeSign extends ForgeBlockState implements Sign {
    public ForgeSign(SignTileEntity blockEntity, IWorld worldAccess) {
        super(blockEntity, worldAccess);
    }

    @Override
    public @NotNull String[] getLines() {
        SignTileEntity sign = (SignTileEntity) blockEntity;

        return new String[] {
                sign.getMessage(0).getString(),
                sign.getMessage(1).getString(),
                sign.getMessage(2).getString(),
                sign.getMessage(3).getString()
        };
    }

    @Override
    public @NotNull String getLine(int index) throws IndexOutOfBoundsException {
        return ((SignTileEntity) blockEntity).getMessage(index).getString();
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        ((SignTileEntity) blockEntity).setMessage(index, new StringTextComponent(line));
    }

    @Override
    public void applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
