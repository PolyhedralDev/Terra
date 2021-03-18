package com.dfsek.terra.fabric.world.block.state;

import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.block.state.Sign;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

public class FabricSign extends FabricBlockState implements Sign {
    public FabricSign(SignBlockEntity blockEntity, WorldAccess worldAccess) {
        super(blockEntity, worldAccess);
    }

    @Override
    public @NotNull String[] getLines() {
        SignBlockEntity sign = (SignBlockEntity) blockEntity;

        return new String[] {
                sign.getTextOnRow(0, false).asString(),
                sign.getTextOnRow(1, false).asString(),
                sign.getTextOnRow(2, false).asString(),
                sign.getTextOnRow(3, false).asString()
        };
    }

    @Override
    public @NotNull String getLine(int index) throws IndexOutOfBoundsException {
        return ((SignBlockEntity) blockEntity).getTextOnRow(index, false).asString();
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        ((SignBlockEntity) blockEntity).setTextOnRow(index, new LiteralText(line));
    }

    @Override
    public void applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            if(!k.startsWith("text")) throw new IllegalArgumentException("Invalid property: " + k);
            setLine(Integer.parseInt(k.substring(4)), v);
        });
    }
}
