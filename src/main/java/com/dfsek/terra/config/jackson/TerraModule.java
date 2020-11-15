package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class TerraModule extends SimpleModule {

    public TerraModule() {
        super("TerraModule", VersionUtil.parseVersion("@projectVersion@", "@projectGroupId@", "@projectArtifactId@"));
    }
}
