/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class ConcatenationOperation extends BinaryOperation<Object, Object> {
    public ConcatenationOperation(Expression<Object> left, Expression<Object> right, SourcePosition position) {
        super(left, right, position);
    }
    
    private static String toString(Object object) {
        String s = object.toString();
        if(object instanceof Double) {
            int l = s.length();
            if(s.charAt(l - 2) == '.' && s.charAt(l - 1) == '0') {
                s = s.substring(0, s.length() - 2);
            }
        }
        return s;
    }
    
    @Override
    public Expression.ReturnType returnType() {
        return Expression.ReturnType.STRING;
    }
    
    @Override
    public Object evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return toString(left.evaluate(implementationArguments, scope)) + toString(right.evaluate(implementationArguments, scope));
    }
}
