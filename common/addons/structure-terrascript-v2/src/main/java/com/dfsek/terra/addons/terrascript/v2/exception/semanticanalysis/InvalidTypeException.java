package com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis;

import com.dfsek.terra.addons.terrascript.v2.exception.CompilationException;
import com.dfsek.terra.addons.terrascript.v2.lexer.SourcePosition;


public class InvalidTypeException extends CompilationException {
    public InvalidTypeException(String message, SourcePosition position) {
        super(message, position);
    }
}
