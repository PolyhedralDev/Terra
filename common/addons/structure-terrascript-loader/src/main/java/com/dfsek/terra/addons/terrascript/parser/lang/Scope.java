package com.dfsek.terra.addons.terrascript.parser.lang;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;


public class Scope {
    private static final Scope NULL = new Scope() {
        @Override
        public Variable<?> get(String id) {
            throw new IllegalStateException("Cannot get variable from null scope: " + id);
        }
        
        @Override
        public void put(String id, Variable<?> variable) {
            throw new IllegalStateException("Cannot set variable in null scope: " + id);
        }
    };
    
    private final Scope parent;
    private final Map<String, Variable<?>> variableMap = new HashMap<>();
    
    public Scope(Scope parent) {
        this.parent = parent;
    }
    
    public Scope() {
        this.parent = NULL;
    }
    
    public Variable<?> get(String id) {
        Variable<?> var = variableMap.get(id);
        return var == null ? parent.get(id) : var;
    }
    
    public void put(String id, Variable<?> variable) {
        variableMap.put(id, variable);
    }
    
    
    public Scope sub() {
        return new Scope(this);
    }
}
