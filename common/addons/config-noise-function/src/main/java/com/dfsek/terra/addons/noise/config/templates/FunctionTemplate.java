package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.Meta;

import java.util.List;

@SuppressWarnings("unused")
public class FunctionTemplate implements ObjectTemplate<FunctionTemplate> {
    @Value("arguments")
    private List<String> args;

    @Value("function")
    private @Meta String function;

    public List<String> getArgs() {
        return args;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public FunctionTemplate get() {
        return this;
    }
}
