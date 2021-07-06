package com.dfsek.terra.config.loaders.config.function;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.List;

@SuppressWarnings("unused")
public class FunctionTemplate implements ObjectTemplate<FunctionTemplate> {
    @Value("arguments")
    private List<String> args;

    @Value("function")
    private String function;

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
