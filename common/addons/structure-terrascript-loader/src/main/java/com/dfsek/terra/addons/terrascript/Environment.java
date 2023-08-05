package com.dfsek.terra.addons.terrascript;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

import com.dfsek.terra.addons.terrascript.Environment.ScopeException.NonexistentSymbolException;
import com.dfsek.terra.addons.terrascript.Environment.ScopeException.SymbolAlreadyExistsException;
import com.dfsek.terra.addons.terrascript.Environment.ScopeException.SymbolTypeMismatchException;
import com.dfsek.terra.addons.terrascript.Environment.Symbol.Function;
import com.dfsek.terra.addons.terrascript.Environment.Symbol.Variable;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class Environment {
    
    private final Environment outer;
    
    private final boolean canAccessOuterVariables;
    
    private final HashMap<String, Symbol> symbolTable = new HashMap<>();
    
    private final boolean inLoop;
    
    private Environment(@Nullable Environment outer, boolean canAccessOuterVariables, boolean inLoop) {
        this.outer = outer;
        this.canAccessOuterVariables = canAccessOuterVariables;
        this.inLoop = inLoop;
    }
    
    public static Environment global() {
        return new Environment(null, false, false);
    }
    
    public Environment lexicalInner() {
        return new Environment(this, true, inLoop);
    }
    
    public Environment loopInner() {
        return new Environment(this, true, true);
    }
    
    public Environment functionalInner() {
        return new Environment(this, false, inLoop);
    }
    
    public Environment outer() {
        if(outer == null) throw new RuntimeException("Attempted to retrieve outer scope of global scope");
        return outer;
    }
    
    public Function getFunction(String id) throws NonexistentSymbolException, SymbolTypeMismatchException {
        Function function;
        Symbol symbol = symbolTable.get(id);
        if(symbol == null) {
            if(outer == null) throw new NonexistentSymbolException();
            function = outer.getFunction(id);
        } else {
            if(!(symbol instanceof Function)) throw new SymbolTypeMismatchException();
            function = (Function) symbol;
        }
        return function;
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
     * @throws NonexistentSymbolException  if symbol is not declared in symbol table
     * @throws SymbolTypeMismatchException if symbol is not a variable
     */
    public Variable getVariable(String id) throws NonexistentSymbolException, SymbolTypeMismatchException {
        Variable variable;
        Symbol symbol = symbolTable.get(id);
        if(symbol == null) {
            if(!canAccessOuterVariables || outer == null) throw new NonexistentSymbolException();
            variable = outer.getVariable(id);
        } else {
            if(!(symbol instanceof Variable)) throw new SymbolTypeMismatchException();
            variable = (Variable) symbol;
        }
        return variable;
    }
    
    public void put(String id, Symbol symbol) throws SymbolAlreadyExistsException {
        if(symbolTable.containsKey(id)) throw new SymbolAlreadyExistsException();
        symbolTable.put(id, symbol);
    }
    
    public static abstract class Symbol {
        
        public static class Function extends Symbol {
            
            public final Type type;
            
            public final List<Pair<String, Type>> parameters;
            
            public Function(Type type, List<Pair<String, Type>> parameters) {
                this.type = type;
                this.parameters = parameters;
            }
        }
        
        
        public static class Variable extends Symbol {
            
            public final Type type;
            
            public Variable(Type type) {
                this.type = type;
            }
        }
    }
    
    
    public static class ScopeException extends RuntimeException {
        
        public static class SymbolAlreadyExistsException extends ScopeException {
        }
        
        
        public static class NonexistentSymbolException extends ScopeException {
        }
        
        
        public static class SymbolTypeMismatchException extends ScopeException {
        }
    }
}
