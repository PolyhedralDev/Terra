package com.dfsek.terra.addons.terrascript.codegen;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.Type;


public interface NativeFunction {
    Map<String, NativeFunction> BUILTIN_FUNCTIONS = new HashMap<>() {{
        put("print", new StaticMethodOfStaticField(
                "java/lang/System",
                "out",
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                Type.VOID,
                List.of(Type.STRING))
           );
        put("printNum", new StaticMethodOfStaticField(
                "java/lang/System",
                "out",
                "java/io/PrintStream",
                "println",
                "(D)V",
                Type.VOID,
                List.of(Type.NUMBER))
           );
    }};
    
    void pushInstance(MethodVisitor method);
    
    void callMethod(MethodVisitor method);
    
    Type getReturnType();
    
    List<Type> getParameterTypes();
    
    class StaticMethodOfStaticField implements NativeFunction {
        
        private final String fieldOwner;
        private final String fieldName;
        private final String className;
        private final String methodName;
        private final String methodDescriptor;
        private final Type returnType;
        private final List<Type> parameters;
        
        // TODO - Use reflection to obtain these automatically
        public StaticMethodOfStaticField(String fieldOwner, String fieldName, String className, String methodName, String methodDescriptor,
                                         Type returnType, List<Type> parameters) {
            this.fieldOwner = fieldOwner;
            this.fieldName = fieldName;
            this.className = className;
            this.methodName = methodName;
            this.methodDescriptor = methodDescriptor;
            this.returnType = returnType;
            this.parameters = parameters;
        }
        
        @Override
        public void pushInstance(MethodVisitor method) {
            method.visitFieldInsn(Opcodes.GETSTATIC, fieldOwner, fieldName, "L" + className + ";");
        }
        
        @Override
        public void callMethod(MethodVisitor method) {
            method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, methodName, methodDescriptor, false);
        }
        
        @Override
        public Type getReturnType() {
            return returnType;
        }
        
        @Override
        public List<Type> getParameterTypes() {
            return parameters;
        }
        
    }
}
