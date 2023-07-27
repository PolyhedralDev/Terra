/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.lexer.Token;
import com.dfsek.terra.addons.terrascript.lexer.Token.TokenType;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;


public class ParserUtil {
    
    private static final Map<TokenType, Map<TokenType, Boolean>> PRECEDENCE = new HashMap<>(); // If second has precedence, true.
    private static final List<TokenType> ARITHMETIC = Arrays.asList(TokenType.ADDITION_OPERATOR, TokenType.SUBTRACTION_OPERATOR,
                                                                    TokenType.MULTIPLICATION_OPERATOR, TokenType.DIVISION_OPERATOR,
                                                                    TokenType.MODULO_OPERATOR);
    private static final List<TokenType> COMPARISON = Arrays.asList(TokenType.EQUALS_OPERATOR, TokenType.NOT_EQUALS_OPERATOR,
                                                                    TokenType.LESS_THAN_OPERATOR, TokenType.LESS_THAN_OR_EQUALS_OPERATOR,
                                                                    TokenType.GREATER_THAN_OPERATOR,
                                                                    TokenType.GREATER_THAN_OR_EQUALS_OPERATOR);
    
    static { // Setup precedence
        Map<TokenType, Boolean> add = new HashMap<>(); // Addition/subtraction before Multiplication/division.
        add.put(TokenType.MULTIPLICATION_OPERATOR, true);
        add.put(TokenType.DIVISION_OPERATOR, true);
        
        PRECEDENCE.put(TokenType.ADDITION_OPERATOR, add);
        PRECEDENCE.put(TokenType.SUBTRACTION_OPERATOR, add);
        
        Map<TokenType, Boolean> numericBoolean = new HashMap<>();
        
        ARITHMETIC.forEach(op -> numericBoolean.put(op, true)); // Numbers before comparison
        COMPARISON.forEach(op -> PRECEDENCE.put(op, numericBoolean));
        
        
        Map<TokenType, Boolean> booleanOps = new HashMap<>();
        ARITHMETIC.forEach(op -> booleanOps.put(op, true)); // Everything before boolean
        COMPARISON.forEach(op -> booleanOps.put(op, true));
        
        
        PRECEDENCE.put(TokenType.BOOLEAN_AND, booleanOps);
        PRECEDENCE.put(TokenType.BOOLEAN_OR, booleanOps);
    }
    
    public static void ensureType(Token token, TokenType... expected) {
        for(TokenType type : expected) if(token.getType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(expected) + " but found " + token.getType(), token.getPosition());
    }
    
    public static void ensureReturnType(Expression<?> returnable, Expression.ReturnType... types) {
        for(Expression.ReturnType type : types) if(returnable.returnType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(types) + " but found " + returnable.returnType(), returnable.getPosition());
    }
    
    public static void checkArithmeticOperation(Expression<?> left, Expression<?> right, Token operation) {
        if(!left.returnType().equals(Expression.ReturnType.NUMBER) || !right.returnType().equals(Expression.ReturnType.NUMBER)) {
            throw new ParseException(
                    "Operation " + operation.getType() + " not supported between " + left.returnType() + " and " + right.returnType(),
                    operation.getPosition());
        }
    }
    
    public static void checkBooleanOperation(Expression<?> left, Expression<?> right, Token operation) {
        if(!left.returnType().equals(Expression.ReturnType.BOOLEAN) || !right.returnType().equals(Expression.ReturnType.BOOLEAN)) {
            throw new ParseException(
                    "Operation " + operation.getType() + " not supported between " + left.returnType() + " and " + right.returnType(),
                    operation.getPosition());
        }
    }
    
    /**
     * Checks if token is a binary operator
     *
     * @param token Token to check
     *
     * @throws ParseException If token isn't a binary operator
     */
    public static void checkBinaryOperator(Token token) {
        if(!token.isBinaryOperator())
            throw new ParseException("Expected binary operator, found " + token.getType(), token.getPosition());
    }
    
    public static Expression.ReturnType getVariableReturnType(Token varToken) {
        return switch(varToken.getType()) {
            case TYPE_NUMBER -> Expression.ReturnType.NUMBER;
            case TYPE_STRING -> Expression.ReturnType.STRING;
            case TYPE_BOOLEAN -> Expression.ReturnType.BOOLEAN;
            case TYPE_VOID -> Expression.ReturnType.VOID;
            default -> throw new ParseException("Unexpected token " + varToken.getType() + "; expected variable declaration",
                                                varToken.getPosition());
        };
    }
    
    public static boolean hasPrecedence(TokenType first, TokenType second) {
        if(!PRECEDENCE.containsKey(first)) return false;
        Map<TokenType, Boolean> pre = PRECEDENCE.get(first);
        if(!pre.containsKey(second)) return false;
        return pre.get(second);
    }
}
