package com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class UserDefinedFunctionBuilder<T extends Function<?>> implements FunctionBuilder<T> {
    
    private final Type returnType;
    private final List<Pair<Integer, Type>> parameterInfo;
    private final Scope.ScopeBuilder bodyScopeBuilder;
    private final Block body;
    
    public UserDefinedFunctionBuilder(Type returnType, List<Pair<Integer, Type>> parameterInfo, Block body,
                                      Scope.ScopeBuilder functionBodyScope) {
        this.returnType = returnType;
        this.bodyScopeBuilder = functionBodyScope;
        this.body = body;
        this.parameterInfo = parameterInfo;
    }
    
    @Override
    public T build(List<Expression<?>> argumentList, SourcePosition position) {
        //noinspection unchecked
        return (T) new Function() {
            
            private final ThreadLocal<Scope> threadLocalScope = ThreadLocal.withInitial(bodyScopeBuilder::build);
            
            @Override
            public Type returnType() {
                return returnType;
            }
            
            @Override
            public Object evaluate(ImplementationArguments implementationArguments, Scope scope) {
                Scope bodyScope = threadLocalScope.get();
                // Pass parameters into scope of function body
                for(int i = 0; i < argumentList.size(); i++) {
                    Pair<Integer, Type> paramInfo = parameterInfo.get(i);
                    Expression<?> argExpression = argumentList.get(i);
                    switch(paramInfo.getRight()) {
                        case NUMBER -> bodyScope.setNum(paramInfo.getLeft(), argExpression.applyDouble(implementationArguments, scope));
                        case BOOLEAN -> bodyScope.setBool(paramInfo.getLeft(), argExpression.applyBoolean(implementationArguments, scope));
                        case STRING -> bodyScope.setStr(paramInfo.getLeft(),
                                                        (String) argExpression.evaluate(implementationArguments, scope));
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
        return parameterInfo.size();
    }
    
    @Override
    public Type getArgument(int position) {
        return parameterInfo.get(position).getRight();
    }
}
