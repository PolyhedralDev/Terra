/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        FunctionTemplate that = (FunctionTemplate) o;
        return args.equals(that.args) && function.equals(that.function) && functions.equals(that.functions);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(args, function, functions);
    }
}
