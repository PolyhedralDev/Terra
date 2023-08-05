package com.dfsek.terra.addons.terrascript.exception.semanticanalysis;

import com.dfsek.terra.addons.terrascript.exception.CompilationException;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class InvalidFunctionDeclarationException extends CompilationException {
    public InvalidFunctionDeclarationException(String message, SourcePosition position) {
        super(message, position);
    }
}
