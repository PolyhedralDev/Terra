package com.dfsek.terra.config;

import org.jetbrains.annotations.NotNull;

public abstract class TerraConfigSection {
    private final TerraConfig parent;

    public TerraConfigSection(@NotNull TerraConfig parent) {
        this.parent = parent;
    }

    @NotNull
    public TerraConfig getParent() {
        return parent;
    }
}
