package com.dfsek.terra.addons.noise.paralithic.defined;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.dynamic.Context;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.paralithic.node.Statefulness;

import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;


public class UserDefinedFunction implements DynamicFunction {
    private final Expression expression;
    private final int args;
    
    protected UserDefinedFunction(Expression expression, int args) {
        this.expression = expression;
        this.args = args;
    }
    
    public static UserDefinedFunction newInstance(FunctionTemplate template, Parser parser, Scope parent) throws ParseException {
        
        Scope functionScope = new Scope().withParent(parent);
        
        template.getArgs().forEach(functionScope::addInvocationVariable);
        
        return new UserDefinedFunction(parser.parse(template.getFunction(), functionScope), template.getArgs().size());
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
