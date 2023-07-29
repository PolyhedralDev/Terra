package com.dfsek.terra.addons.terrascript.parser.lang;


import net.jafama.FastMath;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class Scope {
    private final double[] num;
    private final boolean[] bool;
    private final String[] str;
    
    private Scope(int numSize, int boolSize, int strSize) {
        this.num = new double[numSize];
        this.bool = new boolean[boolSize];
        this.str = new String[strSize];
    }
    
    public double getNum(int index) {
        return num[index];
    }
    
    public boolean getBool(int index) {
        return bool[index];
    }
    
    public String getStr(int index) {
        return str[index];
    }
    
    public void setNum(int index, double value) {
        num[index] = value;
    }
    
    public void setBool(int index, boolean value) {
        bool[index] = value;
    }
    
    public void setStr(int index, String value) {
        str[index] = value;
    }
    
    public static final class ScopeBuilder {
        
        private final Map<String, FunctionBuilder<? extends Function<?>>> functions;
        private final Map<String, Pair<Integer, ReturnType>> indices;
        private int numSize, boolSize, strSize = 0;
        private ScopeBuilder parent;
        
        private boolean inLoop;
        
        public ScopeBuilder() {
            this.functions = new HashMap<>();
            this.indices = new HashMap<>();
        }
        
        private ScopeBuilder(ScopeBuilder parent, boolean inLoop) {
            this.parent = parent;
            this.numSize = parent.numSize;
            this.boolSize = parent.boolSize;
            this.strSize = parent.strSize;
            this.functions = new HashMap<>(parent.functions);
            this.indices = new HashMap<>(parent.indices);
            this.inLoop = inLoop;
        }
        
        private ScopeBuilder(Map<String, FunctionBuilder<? extends Function<?>>> functions) {
            this.functions = new HashMap<>(functions);
            this.indices = new HashMap<>();
        }
        
        public Scope build() {
            return new Scope(numSize, boolSize, strSize);
        }
        
        public ScopeBuilder innerScope() {
            return new ScopeBuilder(this, inLoop);
        }
        
        public ScopeBuilder innerLoopScope() { return new ScopeBuilder(this, true); }
        
        public ScopeBuilder functionScope()  { return new ScopeBuilder(functions); }
        
        public ScopeBuilder registerFunction(String name, FunctionBuilder<? extends Function<?>> functionBuilder) {
            functions.put(name, functionBuilder);
            return this;
        }
        
        public boolean containsFunction(String functionName) {
            return functions.containsKey(functionName);
        }
        
        public FunctionBuilder<?> getFunction(String functionName) {
            return functions.get(functionName);
        }
        
        private String check(String id) {
            if(indices.containsKey(id)) {
                throw new IllegalArgumentException("Variable with ID " + id + " already registered.");
            }
            return id;
        }
        
        public boolean isInLoop() {
            return inLoop;
        }
        
        public int declareNum(String id) {
            int num = numSize;
            indices.put(check(id), Pair.of(num, ReturnType.NUMBER));
            numSize++;
            updateNumSize(numSize);
            return num;
        }
        
        public int declareStr(String id) {
            int str = strSize;
            indices.put(check(id), Pair.of(str, ReturnType.STRING));
            strSize++;
            updateStrSize(strSize);
            return str;
        }
        
        public int declareBool(String id) {
            int bool = boolSize;
            indices.put(check(id), Pair.of(bool, ReturnType.BOOLEAN));
            boolSize++;
            updateBoolSize(boolSize);
            return bool;
        }
        
        private void updateBoolSize(int size) {
            this.boolSize = FastMath.max(boolSize, size);
            if(parent != null) {
                parent.updateBoolSize(size);
            }
        }
        
        private void updateNumSize(int size) {
            this.numSize = FastMath.max(numSize, size);
            if(parent != null) {
                parent.updateNumSize(size);
            }
        }
        
        private void updateStrSize(int size) {
            this.strSize = FastMath.max(strSize, size);
            if(parent != null) {
                parent.updateStrSize(size);
            }
        }
        
        public int getIndex(String id) {
            return indices.get(id).getLeft();
        }
        
        public ReturnType getVaraibleType(String id) {
            return indices.get(id).getRight();
        }
        
        public boolean containsVariable(String id) {
            return indices.containsKey(id);
        }
    }
}
