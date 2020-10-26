package com.dfsek.terra.structure.serialize.block;

import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class SerializableSign implements SerializableBlockState {
    public static final long serialVersionUID = 5298928608478640001L;
    private final String[] text;
    private final boolean isEditable;

    public SerializableSign(Sign orig) {
        text = orig.getLines();
        this.isEditable = orig.isEditable();
    }

    @Override
    public BlockState getState(BlockState orig) {
        if(! (orig instanceof Sign)) throw new IllegalArgumentException("Provided BlockState is not a sign.");
        Sign sign = (Sign) orig;
        for(int i = 0; i < text.length; i++) {
            sign.setLine(i, text[i]);
        }
        sign.setEditable(isEditable);
        return sign;
    }

    public String[] getLines() {
        return text;
    }

    public String getLine(int index) {
        return text[index];
    }

    public boolean isEditable() {
        return isEditable;
    }
}
