package com.dfsek.terra.addons.terrascript.codegen;

import com.dfsek.terra.addons.terrascript.Type;

import com.dfsek.terra.addons.terrascript.exception.CompilerBugException;

import org.objectweb.asm.Opcodes;

import java.util.Map;


public class CodegenType {
    
    private final InstructionType instructionType;
    
    private final String descriptor;
    
    public CodegenType(InstructionType instructionType, String descriptor) {
        this.instructionType = instructionType;
        this.descriptor = descriptor;
    }
    
    public InstructionType bytecodeType() {
        return instructionType;
    }
    
    public String getDescriptor() {
        return descriptor;
    }
    
    public static final CodegenType BOOLEAN_PRIMITIVE = new CodegenType(InstructionType.INTEGER, "Z");
    
    private static final Map<Type, CodegenType> TYPE_MAP = Map.of(
        Type.BOOLEAN, BOOLEAN_PRIMITIVE,
        Type.STRING, new CodegenType(InstructionType.OBJECT, "Ljava/lang/String;"),
        Type.NUMBER, new CodegenType(InstructionType.DOUBLE, "D"),
        Type.VOID, new CodegenType(InstructionType.VOID, "V")
    );
    
    public static CodegenType codegenType(Type type) {
        CodegenType out = TYPE_MAP.get(type);
        if(out == null)
            throw new CompilerBugException();
        return out;
    }
    
    public enum InstructionType {
        DOUBLE {
            @Override
            public int slotSize() {
                return 2;
            }
            
            @Override
            public int returnInsn() {
                return Opcodes.DRETURN;
            }
            
            @Override
            public int loadInsn() {
                return Opcodes.DLOAD;
            }
            
            @Override
            public int storeInsn() {
                return Opcodes.DSTORE;
            }
        },
        OBJECT {
            @Override
            public int slotSize() {
                return 1;
            }
            
            @Override
            public int returnInsn() {
                return Opcodes.ARETURN;
            }
            
            @Override
            public int loadInsn() {
                return Opcodes.ALOAD;
            }
            
            @Override
            public int storeInsn() {
                return Opcodes.ASTORE;
            }
        },
        INTEGER {
            @Override
            public int slotSize() {
                return 1;
            }
            
            @Override
            public int returnInsn() {
                return Opcodes.IRETURN;
            }
            
            @Override
            public int loadInsn() {
                return Opcodes.ILOAD;
            }
            
            @Override
            public int storeInsn() {
                return Opcodes.ISTORE;
            }
        },
        VOID {
            @Override
            public int slotSize() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int returnInsn() {
                return Opcodes.RETURN;
            }
            
            @Override
            public int loadInsn() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int storeInsn() {
                throw new UnsupportedOperationException();
            }
        };
        
        public abstract int slotSize();
        
        public abstract int returnInsn();
        
        public abstract int loadInsn();
        
        public abstract int storeInsn();
    }
}
