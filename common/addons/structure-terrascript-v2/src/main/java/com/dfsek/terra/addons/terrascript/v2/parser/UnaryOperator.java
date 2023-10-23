package com.dfsek.terra.addons.terrascript.v2.parser;

import com.dfsek.terra.addons.terrascript.v2.lexer.Token;


public enum UnaryOperator {
    NOT(Token.TokenType.BANG),
    NEGATE(Token.TokenType.MINUS);
    
    public final Token.TokenType tokenType;
    
    UnaryOperator(Token.TokenType tokenType) { this.tokenType = tokenType; }
}
