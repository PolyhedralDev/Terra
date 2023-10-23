package com.dfsek.terra.addons.terrascript.v2;

import java.util.ArrayList;
import java.util.List;


public class ErrorHandler {
    
    private final List<Exception> exceptions = new ArrayList<>();
    
    public void add(Exception e) {
        exceptions.add(e);
    }
    
    public void throwAny() throws Exception {
        for(Exception e : exceptions) {
            throw e;
        }
    }
}
