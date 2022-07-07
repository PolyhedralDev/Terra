/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.paralithic.defined;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.dynamic.Context;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.paralithic.node.Statefulness;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;


public class UserDefinedFunction implements DynamicFunction {
    private static final Map<FunctionTemplate, UserDefinedFunction> CACHE = new HashMap<>();
    private final Expression expression;
    private final int args;
    
    protected UserDefinedFunction(Expression expression, int args) {
        this.expression = expression;
        this.args = args;
    }
    
    public static UserDefinedFunction newInstance(FunctionTemplate template) throws ParseException {
        UserDefinedFunction function = CACHE.get(template);
        if(function == null) {
            Parser parser = new Parser();
            Scope parent = new Scope();
            
            Scope functionScope = new Scope().withParent(parent);
            
            template.getArgs().forEach(functionScope::addInvocationVariable);
            
            for(Entry<String, FunctionTemplate> entry : template.getFunctions().entrySet()) {
                String id = entry.getKey();
                FunctionTemplate nest = entry.getValue();
                parser.registerFunction(id, newInstance(nest));
            }
            
            function = new UserDefinedFunction(parser.parse(template.getFunction(), functionScope), template.getArgs().size());
            CACHE.put(template, function);
        }
        return function;
    }
    
    @Override
    public double eval(double... args) {
        return expression.evaluate(args);
    }
    
    @Override
    public double eval(Context context, double... args) {
        return expression.evaluate(context, args);
    }
    
    @Override
    public int getArgNumber() {
        return args;
    }
    
    @Override
    public Statefulness statefulness() {
        return Statefulness.STATELESS;
    }
}
