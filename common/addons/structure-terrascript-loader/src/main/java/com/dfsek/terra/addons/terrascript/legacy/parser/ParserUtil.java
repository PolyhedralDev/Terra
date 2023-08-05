/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser;

import java.util.Arrays;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.lexer.Token;


public class ParserUtil {
    
    //    public static void ensureType(Token token, TokenType... expected) {
    //        for(TokenType type : expected) if(token.getType().equals(type)) return;
    //        throw new ParseException("Expected " + Arrays.toString(expected) + " but found " + token.getType(), token.getPosition());
    //    }
    
    public static void ensureReturnType(Expression<?> returnable, Type... types) {
        for(Type type : types) if(returnable.returnType().equals(type)) return;
        throw new ParseException("Invalid type " + returnable.returnType() + ", expected " +
                                 (types.length == 1 ? types[0].toString() : "one of " + Arrays.toString(types)), returnable.getPosition());
    }
    
    public static Type getVariableReturnType(Token varToken) {
        return switch(varToken.type()) {
            case TYPE_NUMBER -> Type.NUMBER;
            case TYPE_STRING -> Type.STRING;
            case TYPE_BOOLEAN -> Type.BOOLEAN;
            case TYPE_VOID -> Type.VOID;
            default -> throw new ParseException("Unexpected token " + varToken.type() + "; expected type",
                                                varToken.position());
        };
    }
}
