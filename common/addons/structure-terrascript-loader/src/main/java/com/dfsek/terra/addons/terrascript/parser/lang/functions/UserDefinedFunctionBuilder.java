package com.dfsek.terra.addons.terrascript.parser.lang.functions;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope.ScopeBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.util.generic.pair.Pair;

import java.util.List;


public class UserDefinedFunctionBuilder<T extends Function<?>> implements FunctionBuilder<T> {
    
    private final ReturnType returnType;
    private final List<Pair<Integer, ReturnType>> argumentInfo;
    private final ScopeBuilder bodyScopeBuilder;
    private final Block body;
    
    public UserDefinedFunctionBuilder(ReturnType returnType, List<Pair<Integer, ReturnType>> argumentInfo, Block body, ScopeBuilder functionBodyScope) {
        this.returnType = returnType;
        this.bodyScopeBuilder = functionBodyScope;
        this.body = body;
        this.argumentInfo = argumentInfo;
    }
    
    @Override
    public T build(List<Expression<?>> argumentList, SourcePosition position) {
        //noinspection unchecked
        return (T) new Function() {
           
            private final ThreadLocal<Scope> threadLocalScope = ThreadLocal.withInitial(bodyScopeBuilder::build);
            
            @Override
            public ReturnType returnType() {
                return returnType;
            }
            
            @Override
            public Object evaluate(ImplementationArguments implementationArguments, Scope scope) {
                Scope bodyScope = threadLocalScope.get();
                // Pass arguments into scope of function body
                for (int i = 0; i < argumentList.size(); i++) {
                    Pair<Integer, ReturnType> argInfo = argumentInfo.get(i);
                    Expression<?> argExpression = argumentList.get(i);
                    switch(argInfo.getRight()) {
                        case NUMBER -> bodyScope.setNum(argInfo.getLeft(), argExpression.applyDouble(implementationArguments, scope));
                        case BOOLEAN -> bodyScope.setBool(argInfo.getLeft(), argExpression.applyBoolean(implementationArguments, scope));
                        case STRING -> bodyScope.setStr(argInfo.getLeft(), (String) argExpression.evaluate(implementationArguments, scope));
                    }
                }
                return body.evaluate(implementationArguments, bodyScope).data().evaluate(implementationArguments, scope);
            }
            
            @Override
            public SourcePosition getPosition() {
                return position;
            }
        };
    }
    
    @Override
    public int argNumber() {
        return argumentInfo.size();
    }
    
    @Override
    public ReturnType getArgument(int position) {
        return argumentInfo.get(position).getRight();
    }
}
