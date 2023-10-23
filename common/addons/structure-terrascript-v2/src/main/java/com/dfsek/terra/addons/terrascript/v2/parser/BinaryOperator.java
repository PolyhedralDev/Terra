package com.dfsek.terra.addons.terrascript.v2.parser;


import com.dfsek.terra.addons.terrascript.v2.lexer.Token;


public enum BinaryOperator {
    BOOLEAN_OR(Token.TokenType.BOOLEAN_OR),
    BOOLEAN_AND(Token.TokenType.BOOLEAN_AND),
    EQUALS(Token.TokenType.EQUALS_EQUALS),
    NOT_EQUALS(Token.TokenType.BANG_EQUALS),
    GREATER(Token.TokenType.GREATER),
    GREATER_EQUALS(Token.TokenType.GREATER_EQUAL),
    LESS(Token.TokenType.LESS),
    LESS_EQUALS(Token.TokenType.LESS_EQUALS),
    ADD(Token.TokenType.PLUS),
    SUBTRACT(Token.TokenType.MINUS),
    MULTIPLY(Token.TokenType.STAR),
    DIVIDE(Token.TokenType.FORWARD_SLASH),
    MODULO(Token.TokenType.MODULO_OPERATOR);
    
    public final Token.TokenType tokenType;
    
    BinaryOperator(Token.TokenType tokenType) { this.tokenType = tokenType; }
}
