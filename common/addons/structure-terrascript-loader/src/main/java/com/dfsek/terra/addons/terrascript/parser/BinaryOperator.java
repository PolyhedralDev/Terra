package com.dfsek.terra.addons.terrascript.parser;


import com.dfsek.terra.addons.terrascript.lexer.Token.TokenType;


public enum BinaryOperator {
    BOOLEAN_OR(TokenType.BOOLEAN_OR),
    BOOLEAN_AND(TokenType.BOOLEAN_AND),
    EQUALS(TokenType.EQUALS_EQUALS),
    NOT_EQUALS(TokenType.BANG_EQUALS),
    GREATER(TokenType.GREATER),
    GREATER_EQUALS(TokenType.GREATER_EQUAL),
    LESS(TokenType.LESS),
    LESS_EQUALS(TokenType.LESS_EQUALS),
    ADD(TokenType.PLUS),
    SUBTRACT(TokenType.MINUS),
    MULTIPLY(TokenType.STAR),
    DIVIDE(TokenType.FORWARD_SLASH),
    MODULO(TokenType.MODULO_OPERATOR);
    
    public final TokenType tokenType;
    
    BinaryOperator(TokenType tokenType) { this.tokenType = tokenType; }
}
