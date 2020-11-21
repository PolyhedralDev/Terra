package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

public class TerraModule extends Module {

    public TerraModule() {
    }

    @Override
    public String getModuleName() {
        return "TerraModule";
    }

    @Override
    public Version version() {
        return new Version(0, 0, 0, null, null, null);
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addDeserializers(new TerraDeserializers());
    }
}
