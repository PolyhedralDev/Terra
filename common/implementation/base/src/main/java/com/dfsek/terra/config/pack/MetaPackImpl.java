package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;

import java.util.Map;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.MetaPack;


public class MetaPackImpl implements MetaPack {
    private final Platform platform;

    private final MetaPackTemplate template = new MetaPackTemplate();

    @Override
    public String getAuthor() {
        return template.getAuthor();
    }

    @Override
    public Version getVersion() {
        return template.getVersion();
    }

    @Override
    public Map<String, ConfigPack> packs() {
        return null;
    }
}
