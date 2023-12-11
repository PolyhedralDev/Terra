package com.dfsek.terra.api.config;

import ca.solostudios.strata.version.Version;

import java.util.Map;


public interface MetaPack {
    Map<String, ConfigPack> packs();

    String getAuthor();

    Version getVersion();
}
