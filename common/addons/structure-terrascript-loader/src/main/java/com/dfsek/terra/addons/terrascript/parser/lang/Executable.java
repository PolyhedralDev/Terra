package com.dfsek.terra.addons.terrascript.parser.lang;


import com.dfsek.terra.addons.terrascript.parser.lang.Scope.ScopeBuilder;


public class Executable {
    private final Block script;
    private final ThreadLocal<Scope> scope;

    public Executable(Block script, ScopeBuilder scopeBuilder) {
        this.script = script;
        this.scope = ThreadLocal.withInitial(scopeBuilder::build);
    }

    public boolean execute(ImplementationArguments arguments) {
        return script.apply(arguments, scope.get()).getLevel() != Block.ReturnLevel.FAIL;
    }
}
