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
    
    public static void ensureType(Token token, TokenType... expected) {
        for(TokenType type : expected) if(token.getType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(expected) + " but found " + token.getType(), token.getPosition());
    }
    
    public static void ensureReturnType(Expression<?> returnable, Expression.ReturnType... types) {
        for(Expression.ReturnType type : types) if(returnable.returnType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(types) + " but found " + returnable.returnType(), returnable.getPosition());
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
}
