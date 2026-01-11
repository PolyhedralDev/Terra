package com.dfsek.terra.addons.shortcut.blockstate;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;

import com.dfsek.tectonic.api.exception.LoadException;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class FallbackConfigType implements ConfigType<BlockStateFallbackTemplate, BlockState>, ConfigFactory<BlockStateFallbackTemplate, BlockState>  {
    @Override
    public BlockStateFallbackTemplate getTemplate(ConfigPack pack, Platform platform) {
        return null;
    }

    @Override
    public ConfigFactory<BlockStateFallbackTemplate, BlockState> getFactory() {
        return this;
    }

    @Override
    public TypeKey<BlockState> getTypeKey() {
        return null;
    }

    @Override
    public BlockState build(BlockStateFallbackTemplate config, Platform platform) throws LoadException {
        return null;
    }
}
