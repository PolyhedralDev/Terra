package com.dfsek.terra.addons.terrascript.v2.codegen;

import org.objectweb.asm.Opcodes;


public class CodegenType {
    
    public static final CodegenType BOOLEAN = new CodegenType(InstructionType.INTEGER, "Z");
    public static final CodegenType STRING = new CodegenType(InstructionType.OBJECT, "Ljava/lang/String;");
    public static final CodegenType DOUBLE = new CodegenType(InstructionType.DOUBLE, "D");
    public static final CodegenType INTEGER = new CodegenType(InstructionType.INTEGER, "I");
    public static final CodegenType VOID = new CodegenType(InstructionType.VOID, "V");
    public static final CodegenType OBJECT = new CodegenType(InstructionType.OBJECT, "Ljava/lang/Object;");
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
