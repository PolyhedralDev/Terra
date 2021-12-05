/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.LinkedHashMap;
import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;


@SuppressWarnings("unused")
public class FunctionTemplate implements ObjectTemplate<FunctionTemplate> {
    @Value("arguments")
    private List<String> args;
    
    @Value("expression")
    private @Meta String function;
    
    @Value("functions")
    @Default
    private @Meta LinkedHashMap<String, @Meta FunctionTemplate> functions = new LinkedHashMap<>();
    
    @Override
    public FunctionTemplate get() {
        return this;
    }
    
    public List<String> getArgs() {
        return args;
    }
    
    public String getFunction() {
        return function;
    }
    
    public LinkedHashMap<String, FunctionTemplate> getFunctions() {
        return functions;
    }
}
