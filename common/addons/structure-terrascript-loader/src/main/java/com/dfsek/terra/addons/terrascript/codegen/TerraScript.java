package com.dfsek.terra.addons.terrascript.codegen;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public interface TerraScript {
    
    void execute();
    
    Map<String, Method> BUILTIN_FUNCTIONS = new HashMap<>() {{
        try {
            put("print", System.out.getClass().getMethod("println", String.class));
            put("printNum", System.out.getClass().getMethod("println", double.class));
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }};
}
