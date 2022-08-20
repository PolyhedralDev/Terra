package com.dfsek.terra.addons.manifest.impl;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;

import java.util.function.Function;


public record InitInfo(
        Platform platform,
        BaseAddon addon
) {
}
