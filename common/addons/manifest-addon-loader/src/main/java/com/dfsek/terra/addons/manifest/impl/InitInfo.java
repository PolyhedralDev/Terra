package com.dfsek.terra.addons.manifest.impl;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;


public record InitInfo(
        Platform platform,
        BaseAddon addon
) {
}
