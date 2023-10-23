/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.tokenizer;

public class Token {
    private final String content;
    private final Type type;
    private final Position start;
    
    public Token(String content, Type type, Position start) {
        this.content = content;
        this.type = type;
        this.start = start;
    }
    
    @Override
    public String toString() {
        return type + ": '" + content + "'";
    }
    
    public Type getType() {
        return type;
    }
    
    public String getContent() {
        return content;
    }
    
    public Position getPosition() {
        return start;
    }
    
    public boolean isConstant() {
        return this.type.equals(Type.NUMBER) || this.type.equals(Type.STRING) || this.type.equals(Type.BOOLEAN);
    }
    
    public boolean isBinaryOperator() {
        return type.equals(Type.ADDITION_OPERATOR)
               || type.equals(Type.SUBTRACTION_OPERATOR)
               || type.equals(Type.MULTIPLICATION_OPERATOR)
               || type.equals(Type.DIVISION_OPERATOR)
               || type.equals(Type.EQUALS_OPERATOR)
               || type.equals(Type.NOT_EQUALS_OPERATOR)
               || type.equals(Type.LESS_THAN_OPERATOR)
               || type.equals(Type.GREATER_THAN_OPERATOR)
               || type.equals(Type.LESS_THAN_OR_EQUALS_OPERATOR)
               || type.equals(Type.GREATER_THAN_OR_EQUALS_OPERATOR)
               || type.equals(Type.BOOLEAN_OR)
               || type.equals(Type.BOOLEAN_AND)
               || type.equals(Type.MODULO_OPERATOR);
    }
    
    public boolean isStrictNumericOperator() {
        return type.equals(Type.SUBTRACTION_OPERATOR)
               || type.equals(Type.MULTIPLICATION_OPERATOR)
               || type.equals(Type.DIVISION_OPERATOR)
               || type.equals(Type.GREATER_THAN_OPERATOR)
               || type.equals(Type.LESS_THAN_OPERATOR)
               || type.equals(Type.LESS_THAN_OR_EQUALS_OPERATOR)
               || type.equals(Type.GREATER_THAN_OR_EQUALS_OPERATOR)
               || type.equals(Type.MODULO_OPERATOR);
    }
    
    public boolean isStrictBooleanOperator() {
        return type.equals(Type.BOOLEAN_AND)
               || type.equals(Type.BOOLEAN_OR);
    }
    
    public boolean isVariableDeclaration() {
        return type.equals(Type.STRING_VARIABLE)
               || type.equals(Type.BOOLEAN_VARIABLE)
               || type.equals(Type.NUMBER_VARIABLE);
    }
    
    public boolean isLoopLike() {
        return type.equals(Type.IF_STATEMENT)
               || type.equals(Type.WHILE_LOOP)
               || type.equals(Type.FOR_LOOP);
    }
    
    public boolean isIdentifier() {
        return type.equals(Type.IDENTIFIER);
    }
    
    public enum Type {
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
        GROUP_BEGIN,
        /**
         * Ending of group
         */
        GROUP_END,
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
        EQUALS_OPERATOR,
        /**
         * Boolean not equals operator
         */
        NOT_EQUALS_OPERATOR,
        /**
         * Boolean greater than operator
         */
        GREATER_THAN_OPERATOR,
        /**
         * Boolean less than operator
         */
        LESS_THAN_OPERATOR,
        /**
         * Boolean greater than or equal to operator
         */
        GREATER_THAN_OR_EQUALS_OPERATOR,
        /**
         * Boolean less than or equal to operator
         */
        LESS_THAN_OR_EQUALS_OPERATOR,
        /**
         * Addition/concatenation operator
         */
        ADDITION_OPERATOR,
        /**
         * Subtraction operator
         */
        SUBTRACTION_OPERATOR,
        /**
         * Multiplication operator
         */
        MULTIPLICATION_OPERATOR,
        /**
         * Division operator
         */
        DIVISION_OPERATOR,
        /**
         * Modulo operator.
         */
        MODULO_OPERATOR,
        /**
         * Boolean not operator
         */
        BOOLEAN_NOT,
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
        NUMBER_VARIABLE,
        /**
         * String variable declaration
         */
        STRING_VARIABLE,
        /**
         * Boolean variable declaration
         */
        BOOLEAN_VARIABLE,
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
        ELSE
    }
}
