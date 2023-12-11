package com.dfsek.terra.api.config;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;

import com.dfsek.terra.api.addon.BaseAddon;

import java.util.Map;


public interface MetaPack {
    Map<String, ConfigPack> packs();

    String getAuthor();

    Version getVersion();
}
