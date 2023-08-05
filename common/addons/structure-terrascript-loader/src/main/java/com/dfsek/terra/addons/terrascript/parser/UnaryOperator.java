package com.dfsek.terra.addons.terrascript.parser;

import com.dfsek.terra.addons.terrascript.lexer.Token.TokenType;


public enum UnaryOperator {
    NOT(TokenType.BANG),
    NEGATE(TokenType.MINUS);
    
    public final TokenType tokenType;
    
    UnaryOperator(TokenType tokenType) { this.tokenType = tokenType; }
}
