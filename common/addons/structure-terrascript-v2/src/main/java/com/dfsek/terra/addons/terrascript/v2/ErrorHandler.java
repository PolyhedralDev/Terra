package com.dfsek.terra.addons.terrascript.v2;

import com.dfsek.terra.addons.terrascript.v2.exception.CompilationException;

import java.util.ArrayList;
import java.util.List;


public class ErrorHandler {
    
    private final List<CompilationException> exceptions = new ArrayList<>();
    
    public void add(CompilationException e) {
        exceptions.add(e);
    }
    
    public void throwAny() throws CompilationException {
        for(CompilationException e : exceptions) {
            throw e;
        }
    }
}
