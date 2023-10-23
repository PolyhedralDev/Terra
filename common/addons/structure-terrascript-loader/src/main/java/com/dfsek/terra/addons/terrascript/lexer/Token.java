/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.lexer;

import java.util.Objects;


public class Token {
    private final String lexeme;
    private final TokenType type;
    private final SourcePosition start;
    
    public Token(String lexeme, TokenType type, SourcePosition start) {
        this.lexeme = type == TokenType.END_OF_FILE ? "END OF FILE" : lexeme;
        this.type = type;
        this.start = start;
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(lexeme, token.lexeme) && type == token.type && Objects.equals(start, token.start);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lexeme, type, start);
    }
    
    @Override
    public String toString() {
        return type + ": '" + lexeme + "'";
    }
    
    public TokenType type() {
        return type;
    }
    
    public String lexeme() {
        return lexeme;
    }
    
    public SourcePosition position() {
        return start;
    }
    
    public boolean isType(TokenType... types) {
        for(TokenType t : types) if(t == type) return true;
        return false;
    }
    
    public enum TokenType {
        /**
         * Function identifier or language keyword
         */
        IDENTIFIER,
        
        /**
         * Numeric literal
         */
        NUMBER,
        /**
         * String literal
         */
        STRING,
        /**
         * Boolean literal
         */
        BOOLEAN,
        /**
         * Beginning of group
         */
        OPEN_PAREN,
        /**
         * Ending of group
         */
        CLOSE_PAREN,
        /**
         * End of statement
         */
        STATEMENT_END,
        /**
         * Argument separator
         */
        SEPARATOR,
        /**
         * Beginning of code block
         */
        BLOCK_BEGIN,
        /**
         * End of code block
         */
        BLOCK_END,
        /**
         * assignment operator
         */
        ASSIGNMENT,
        /**
         * Boolean equals operator
         */
        EQUALS_EQUALS,
        /**
         * Boolean not equals operator
         */
        BANG_EQUALS,
        /**
         * Boolean greater than operator
         */
        GREATER,
        /**
         * Boolean less than operator
         */
        LESS,
        /**
         * Boolean greater than or equal to operator
         */
        GREATER_EQUAL,
        /**
         * Boolean less than or equal to operator
         */
        LESS_EQUALS,
        /**
         * Addition/concatenation operator
         */
        PLUS,
        /**
         * Subtraction operator
         */
        MINUS,
        /**
         * Multiplication operator
         */
        STAR,
        /**
         * Division operator
         */
        FORWARD_SLASH,
        /**
         * Modulo operator.
         */
        MODULO_OPERATOR,
        /**
         * Boolean not operator
         */
        BANG,
        /**
         * Boolean or
         */
        BOOLEAN_OR,
        /**
         * Boolean and
         */
        BOOLEAN_AND,
        /**
         * Variable declaration
         */
        VARIABLE,
        /**
         * Function declaration
         */
        FUNCTION,
        COLON,
        /**
         * If statement declaration
         */
        IF_STATEMENT,
        /**
         * While loop declaration
         */
        WHILE_LOOP,
        /**
         * Return statement
         */
        RETURN,
        /**
         * Continue statement
         */
        CONTINUE,
        /**
         * Break statement
         */
        BREAK,
        /**
         * Fail statement. Like return keyword, but specifies that generation has failed.
         */
        FAIL,
        /**
         * For loop initializer token
         */
        FOR_LOOP,
        /**
         * Else keyword
         */
        ELSE,
        /**
         * End of file
         */
        END_OF_FILE
    }
}
