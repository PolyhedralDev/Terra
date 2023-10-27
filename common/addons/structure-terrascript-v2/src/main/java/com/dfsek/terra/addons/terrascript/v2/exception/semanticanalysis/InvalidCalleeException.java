package com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis;

import com.dfsek.terra.addons.terrascript.v2.exception.CompilationException;
import com.dfsek.terra.addons.terrascript.v2.lexer.SourcePosition;


public class InvalidCalleeException extends CompilationException {
    public InvalidCalleeException(String message, SourcePosition position) {
        super(message, position);
    }
}
