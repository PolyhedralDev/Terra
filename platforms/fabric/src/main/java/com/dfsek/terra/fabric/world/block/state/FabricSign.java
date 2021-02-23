package com.dfsek.terra.fabric.world.block.state;

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
        return new String[0];
    }

    @Override
    public @NotNull String getLine(int index) throws IndexOutOfBoundsException {
        return ((SignBlockEntity) blockEntity).getTextOnRow(index).asString();
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        ((SignBlockEntity) blockEntity).setTextOnRow(index, new LiteralText(line));
    }
}
