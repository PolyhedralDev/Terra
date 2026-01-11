package com.dfsek.terra.addons.shortcut.blockstate;

import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;

import java.util.List;


public class BlockStateFallbackTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;

    @Value("target")
    private @Meta String target;

    @Value("alternatives")
    private @Meta List<@Meta String> alternatives;

    @Override
    public String getID() {
        return id;
    }

    public String getTarget() {
        return target;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }
}
