/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.lexer;

public class Token {
    private final String content;
    private final TokenType type;
    private final SourcePosition start;
    
    public Token(String content, TokenType type, SourcePosition start) {
        this.content = content;
        this.type = type;
        this.start = start;
    }
    
    @Override
    public String toString() {
        return type + ": '" + content + "'";
    }
    
    public TokenType getType() {
        return type;
    }
    
    public String getContent() {
        return content;
    }
    
    public SourcePosition getPosition() {
        return start;
    }
    
    public boolean isConstant() {
        return this.type.equals(TokenType.NUMBER) || this.type.equals(TokenType.STRING) || this.type.equals(TokenType.BOOLEAN);
    }
    
    public boolean isType(TokenType type) {
        return type == getType();
    }
    
    public boolean isBinaryOperator() {
        return type.equals(TokenType.PLUS)
               || type.equals(TokenType.MINUS)
               || type.equals(TokenType.STAR)
               || type.equals(TokenType.FORWARD_SLASH)
               || type.equals(TokenType.EQUALS_EQUALS)
               || type.equals(TokenType.BANG_EQUALS)
               || type.equals(TokenType.LESS)
               || type.equals(TokenType.GREATER)
               || type.equals(TokenType.LESS_EQUALS)
               || type.equals(TokenType.GREATER_EQUAL)
               || type.equals(TokenType.BOOLEAN_OR)
               || type.equals(TokenType.BOOLEAN_AND)
               || type.equals(TokenType.MODULO_OPERATOR);
    }
    
    public boolean isStrictNumericOperator() {
        return type.equals(TokenType.MINUS)
               || type.equals(TokenType.STAR)
               || type.equals(TokenType.FORWARD_SLASH)
               || type.equals(TokenType.GREATER)
               || type.equals(TokenType.LESS)
               || type.equals(TokenType.LESS_EQUALS)
               || type.equals(TokenType.GREATER_EQUAL)
               || type.equals(TokenType.MODULO_OPERATOR);
    }
    
    public boolean isStrictBooleanOperator() {
        return type.equals(TokenType.BOOLEAN_AND)
               || type.equals(TokenType.BOOLEAN_OR);
    }
    
    public boolean isVariableDeclaration() {
        return type.equals(TokenType.TYPE_STRING)
               || type.equals(TokenType.TYPE_BOOLEAN)
               || type.equals(TokenType.TYPE_NUMBER);
    }
    
    public boolean isControlStructure() {
        return type.equals(TokenType.IF_STATEMENT)
               || type.equals(TokenType.WHILE_LOOP)
               || type.equals(TokenType.FOR_LOOP);
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
         * Numeric variable declaration
         */
        TYPE_NUMBER,
        /**
         * String variable declaration
         */
        TYPE_STRING,
        /**
         * Boolean variable declaration
         */
        TYPE_BOOLEAN,
        /**
         * Void type declaration
         */
        TYPE_VOID,
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
