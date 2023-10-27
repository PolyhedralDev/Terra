package com.dfsek.terra.addons.terrascript.v2;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.v2.Environment.ScopeException.NonexistentSymbolException;
import com.dfsek.terra.addons.terrascript.v2.Environment.ScopeException.SymbolAlreadyExistsException;
import com.dfsek.terra.addons.terrascript.v2.codegen.NativeFunction;


public class Environment {
    
    public final String name;
    private final Environment outer;
    private final boolean canAccessOuterVariables;
    private final Map<String, Symbol> symbolTable = new HashMap<>();
    private final boolean inLoop;
    private final int index;
    private int innerCount = 0;
    
    private Environment(@Nullable Environment outer, boolean canAccessOuterVariables, boolean inLoop, int index) {
        this.outer = outer;
        this.canAccessOuterVariables = canAccessOuterVariables;
        this.inLoop = inLoop;
        this.index = index;
        this.name = String.join("_", getNestedIndexes().stream().map(Object::toString).toList());
        // Populate global scope with built-in Java implemented methods
        // TODO - Replace with AST import nodes
        if(index == 0) NativeFunction.BUILTIN_FUNCTIONS.forEach((name, function) ->
                                                                        symbolTable.put(name, new Symbol.Variable(
                                                                                new Type.Function.Native(function.getReturnType(),
                                                                                                         function.getParameterTypes(), name,
                                                                                                         this, function))));
    }
    
    public static Environment global() {
        return new Environment(null, false, false, 0);
    }
    
    public Environment lexicalInner() {
        return new Environment(this, true, inLoop, innerCount++);
    }
    
    public Environment loopInner() {
        return new Environment(this, true, true, innerCount++);
    }
    
    public Environment functionalInner() {
        return new Environment(this, false, inLoop, innerCount++);
    }
    
    private List<Integer> getNestedIndexes() {
        List<Integer> idxs = new ArrayList<>();
        for(Environment env = this; env.outer != null; env = env.outer) {
            idxs.add(0, env.index);
        }
        return idxs;
    }
    
    public Environment outer() {
        if(outer == null) throw new RuntimeException("Attempted to retrieve outer scope of global scope");
        return outer;
    }
    
    /**
     * Returns symbol table entry for a variable identifier, includes enclosing scopes in lookup.
     * <br>
     * Does not factor context of lookup, checks for order of declaration should be done while
     * symbol tables are being populated.
     *
     * @param id identifier used in variable declaration
     *
     * @return variable symbol table entry
     *
     * @throws NonexistentSymbolException if symbol is not declared in symbol table
     */
    public Symbol getVariable(String id) throws NonexistentSymbolException {
        Symbol symbol = symbolTable.get(id);
        if(symbol != null) return symbol;
        if(outer == null) throw new NonexistentSymbolException();
        if(canAccessOuterVariables) return outer.getVariable(id);
        
        // Only functions can be accessed from restricted scopes
        // TODO - Only make applicable to functions that cannot be reassigned
        Symbol potentialFunction = outer.getVariableUnrestricted(id);
        if(!(potentialFunction.type instanceof Type.Function)) throw new NonexistentSymbolException();
        return potentialFunction;
    }
    
    private Symbol getVariableUnrestricted(String id) throws NonexistentSymbolException {
        Symbol symbol = symbolTable.get(id);
        if(symbol != null) return symbol;
        if(outer == null) throw new NonexistentSymbolException();
        return outer.getVariableUnrestricted(id);
    }
    
    public void put(String id, Symbol symbol) throws SymbolAlreadyExistsException {
        if(symbolTable.containsKey(id)) throw new SymbolAlreadyExistsException();
        symbolTable.put(id, symbol);
    }
    
    public static abstract class Symbol {
        
        public final Type type;
        
        public Symbol(Type type) {
            this.type = type;
        }
        
        public static class Variable extends Symbol {
            
            public Variable(Type type) {
                super(type);
            }
        }
    }
    
    
    public static class ScopeException extends Exception {
        
        public static class SymbolAlreadyExistsException extends ScopeException {
        }
        
        
        public static class NonexistentSymbolException extends ScopeException {
        }
        
    }
}
