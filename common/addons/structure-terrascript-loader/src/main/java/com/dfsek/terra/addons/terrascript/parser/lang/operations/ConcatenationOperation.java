/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class ConcatenationOperation extends BinaryOperation<Object, Object> {
    public ConcatenationOperation(Returnable<Object> left, Returnable<Object> right, Position position) {
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
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.STRING;
    }

    @Override
    public Object apply(ImplementationArguments implementationArguments, Scope scope) {
        return toString(left.apply(implementationArguments, scope)) + toString(right.apply(implementationArguments, scope));
    }
}
