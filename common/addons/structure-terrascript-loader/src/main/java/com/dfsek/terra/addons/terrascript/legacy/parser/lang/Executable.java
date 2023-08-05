package com.dfsek.terra.addons.terrascript.legacy.parser.lang;


public class Executable {
    private final Block script;
    private final ThreadLocal<Scope> scope;
    
    public Executable(Block script, Scope.ScopeBuilder scopeBuilder) {
        this.script = script;
        this.scope = ThreadLocal.withInitial(scopeBuilder::build);
    }
    
    public boolean execute(ImplementationArguments arguments) {
        return script.evaluate(arguments, scope.get()).level() != Block.EvaluationLevel.FAIL;
    }
}
