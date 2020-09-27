package com.dfsek.terra.structure.serialize.block;

import org.bukkit.block.BlockState;

import java.io.Serializable;

public interface SerializableBlockState extends Serializable {
    long serialVersionUID = 5298928608478640000L;
    BlockState getState(BlockState orig);
}
