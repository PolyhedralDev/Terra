package com.dfsek.terra.addons.terrascript.codegen.asm;

import com.dfsek.terra.addons.terrascript.Environment.Symbol;
import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.ast.Expr;
import com.dfsek.terra.addons.terrascript.ast.Expr.Assignment;
import com.dfsek.terra.addons.terrascript.ast.Expr.Binary;
import com.dfsek.terra.addons.terrascript.ast.Expr.Call;
import com.dfsek.terra.addons.terrascript.ast.Expr.Grouping;
import com.dfsek.terra.addons.terrascript.ast.Expr.Literal;
import com.dfsek.terra.addons.terrascript.ast.Expr.Unary;
import com.dfsek.terra.addons.terrascript.ast.Expr.Variable;
import com.dfsek.terra.addons.terrascript.ast.Expr.Void;
import com.dfsek.terra.addons.terrascript.ast.Stmt;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Block;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Break;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Continue;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Expression;
import com.dfsek.terra.addons.terrascript.ast.Stmt.For;
import com.dfsek.terra.addons.terrascript.ast.Stmt.FunctionDeclaration;
import com.dfsek.terra.addons.terrascript.ast.Stmt.If;
import com.dfsek.terra.addons.terrascript.ast.Stmt.NoOp;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Return;
import com.dfsek.terra.addons.terrascript.ast.Stmt.VariableDeclaration;
import com.dfsek.terra.addons.terrascript.ast.Stmt.While;

import com.dfsek.terra.addons.terrascript.codegen.TerraScript;
import com.dfsek.terra.addons.terrascript.util.ASMUtil;
import com.dfsek.terra.api.util.generic.pair.Pair;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dfsek.terra.addons.terrascript.util.ASMUtil.dynamicName;

public class TerraScriptClassGenerator {
    
    private static final Class<?> TARGET_CLASS = TerraScript.class;
    
    private static final boolean DUMP = true;
    
    private int generationCount = 0;
    
    private final String debugPath;
    
    public TerraScriptClassGenerator(String debugPath) {
        this.debugPath = debugPath;
    }
    
    /**
     *
     * @param root Assumed to be semantically correct
     * @return Generated TerraScript instance
     * @throws IOException
     */
    public TerraScript generate(Block root) throws IOException {
        String targetClassName = dynamicName(TARGET_CLASS);
        String generatedClassName = targetClassName + "_GENERATED_" + generationCount;
        
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        
        // Create class
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, generatedClassName, null, "java/lang/Object", new String[]{ targetClassName });
       
        // Generate constructor method
        MethodVisitor constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0); // Put this reference on stack
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN); // Void return
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();
        
        // Generate execute method
        String methodName = "execute";
        // Extract method description
        MethodExtractor extractor = new MethodExtractor(methodName);
        new ClassReader(targetClassName).accept(extractor, 0);
        String description = extractor.methodDescription;
        int exeAcc = Opcodes.ACC_PUBLIC;
        MethodVisitor executeMethod = classWriter.visitMethod(exeAcc, methodName, description, null, null);
        executeMethod.visitCode(); // Start method body
        new MethodBytecodeGenerator(classWriter, generatedClassName, executeMethod, exeAcc, description).generate(root); // Generate bytecode
        // Finish up method
        executeMethod.visitInsn(Opcodes.RETURN);
        executeMethod.visitMaxs(0, 0);
        executeMethod.visitEnd();
        
        // Finished generating class
        classWriter.visitEnd();
        
        DynamicClassLoader loader = new DynamicClassLoader(TARGET_CLASS); // Instantiate a new loader every time so classes can be GC'ed when they are no longer used. (Classes cannot be GC'ed until their loaders are).
        
        generationCount++;
        
        byte[] bytecode = classWriter.toByteArray();
        
        Class<?> generatedClass = loader.defineClass(generatedClassName.replace('/', '.'), bytecode);
        
        if (DUMP) {
            File dump = new File(debugPath + "/" + generatedClass.getSimpleName() + ".class");
            dump.getParentFile().mkdirs();
            try(FileOutputStream out = new FileOutputStream(dump)) {
                out.write(bytecode);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            Object instance = generatedClass.getDeclaredConstructor().newInstance();
            return (TerraScript) instance;
        } catch(ReflectiveOperationException e) {
            throw new Error(e); // Should literally never happen
        }
    }
    
    private static class MethodBytecodeGenerator implements Stmt.Visitor<Void>, Expr.Visitor<Void> {
        
        private final ClassWriter classWriter;
        
        private final String className;
        
        private final MethodVisitor method;
        
        private final String descriptor;
        
        private final LocalVariablesSorter lvs;
        
        private final Map<String, Integer> lvTable = new HashMap<>();
        
        public MethodBytecodeGenerator(ClassWriter classWriter, String className, MethodVisitor method, int access, String descriptor) {
            this.classWriter = classWriter;
            this.className = className;
            this.method = method;
            this.descriptor = descriptor;
            this.lvs = new LocalVariablesSorter(access, descriptor, method);
        }
        
        public void generate(Block root) {
            this.visitBlockStmt(root);
            
        }
        
        @Override
        public Void visitBinaryExpr(Binary expr) {
            expr.left.accept(this);
            expr.right.accept(this);
            switch(expr.operator) {
                // TODO - Short circuit binary operators
                case BOOLEAN_OR -> method.visitInsn(Opcodes.IOR);
                case BOOLEAN_AND -> method.visitInsn(Opcodes.IAND);
//                case EQUALS -> null;
//                case NOT_EQUALS -> null;
                case GREATER -> {
                    Label falseLabel = new Label();
                    Label finished = new Label();
                    method.visitInsn(Opcodes.DCMPL);
                    method.visitJumpInsn(Opcodes.IFLE, falseLabel);
                    method.visitInsn(Opcodes.ICONST_1);
                    method.visitJumpInsn(Opcodes.GOTO, finished);
                    method.visitLabel(falseLabel);
                    method.visitInsn(Opcodes.ICONST_0);
                    method.visitLabel(finished);
                }
                case GREATER_EQUALS -> {
                    Label falseLabel = new Label();
                    Label finished = new Label();
                    method.visitInsn(Opcodes.DCMPL);
                    method.visitJumpInsn(Opcodes.IFLT, falseLabel);
                    method.visitInsn(Opcodes.ICONST_1);
                    method.visitJumpInsn(Opcodes.GOTO, finished);
                    method.visitLabel(falseLabel);
                    method.visitInsn(Opcodes.ICONST_0);
                    method.visitLabel(finished);
                }
                case LESS -> {
                    Label falseLabel = new Label();
                    Label finished = new Label();
                    method.visitInsn(Opcodes.DCMPG);
                    method.visitJumpInsn(Opcodes.IFGE, falseLabel);
                    method.visitInsn(Opcodes.ICONST_1);
                    method.visitJumpInsn(Opcodes.GOTO, finished);
                    method.visitLabel(falseLabel);
                    method.visitInsn(Opcodes.ICONST_0);
                    method.visitLabel(finished);
                }
                case LESS_EQUALS -> {
                    Label falseLabel = new Label();
                    Label finished = new Label();
                    method.visitInsn(Opcodes.DCMPG);
                    method.visitJumpInsn(Opcodes.IFGT, falseLabel);
                    method.visitInsn(Opcodes.ICONST_1);
                    method.visitJumpInsn(Opcodes.GOTO, finished);
                    method.visitLabel(falseLabel);
                    method.visitInsn(Opcodes.ICONST_0);
                    method.visitLabel(finished);
                }
                case ADD -> {
                    switch(expr.getType()) {
                        case NUMBER -> method.visitInsn(Opcodes.DADD);
                        // TODO - Optimize string concatenation
                        case STRING -> method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
                        default -> throw new RuntimeException("Could not generate bytecode for ADD binary operator returning type " + expr.getType());
                    }
                }
                case SUBTRACT -> method.visitInsn(Opcodes.DSUB);
                case MULTIPLY -> method.visitInsn(Opcodes.DMUL);
                case DIVIDE -> method.visitInsn(Opcodes.DDIV);
//                case MODULO ->
            }
            return null;
        }
        
        @Override
        public Void visitGroupingExpr(Grouping expr) {
            expr.expression.accept(this);
            return null;
        }
        
        @Override
        public Void visitLiteralExpr(Literal expr) {
            method.visitLdcInsn(expr.value);
            return null;
        }
        
        @Override
        public Void visitUnaryExpr(Unary expr) {
            return null;
        }
        
        @Override
        public Void visitCallExpr(Call expr) {
            Symbol.Function function = expr.getSymbol();
            if (TerraScript.BUILTIN_FUNCTIONS.containsKey(expr.identifier)) {
                if (expr.identifier.equals("print")) { // TODO - remove quick dirty print function call
                    method.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    expr.arguments.get(0).accept(this);
                    method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }
                return null;
            }
            expr.arguments.forEach(a -> a.accept(this));
            List<Type> parameters = function.parameters.stream().map(Pair::getRight).toList();
            method.visitMethodInsn(Opcodes.INVOKESTATIC, className, expr.identifier, getFunctionDescriptor(parameters, function.type), false);
            return null;
        }
        
        @Override
        public Void visitVariableExpr(Variable expr) {
            Type varType = expr.getSymbol().type;
            method.visitVarInsn(switch(varType) {
                case NUMBER -> Opcodes.DLOAD;
                case STRING -> Opcodes.ALOAD;
                case BOOLEAN -> Opcodes.ILOAD;
                default -> throw new RuntimeException("Unable to load local variable, unknown parameter type '" + varType + "'");
            }, lvTable.get(expr.identifier));
            return null;
        }
        
        @Override
        public Void visitAssignmentExpr(Assignment expr) {
            expr.rValue.accept(this);
            Type type = expr.lValue.getSymbol().type;
            method.visitVarInsn(switch(type) {
                case NUMBER -> Opcodes.DSTORE;
                case STRING -> Opcodes.ASTORE;
                case BOOLEAN -> Opcodes.ISTORE;
                default -> throw new RuntimeException("Unable to assign local variable, unknown parameter type '" + type + "'");
            }, lvTable.get(expr.lValue.identifier));
            return null;
        }
        
        @Override
        public Void visitVoidExpr(Void expr) {
            return null;
        }
        
        @Override
        public Void visitExpressionStmt(Expression stmt) {
            stmt.expression.accept(this);
            return null;
        }
        
        @Override
        public Void visitBlockStmt(Block stmt) {
            stmt.statements.forEach(s -> s.accept(this));
            return null;
        }
        
        private String getFunctionDescriptor(List<Type> parameters, Type returnType) {
            StringBuilder sb = new StringBuilder().append("(");
            parameters.stream().map(p -> switch (p) {
                case NUMBER -> "D";
                case STRING -> "Ljava/lang/String;";
                case BOOLEAN -> "Z";
                default -> throw new RuntimeException("Unable to generate method descriptor, unknown parameter type '" + p + "'");
            }).forEach(sb::append);
            sb.append(")");
            sb.append(switch (returnType) {
                case NUMBER -> "D";
                case STRING -> "Ljava/lang/String;";
                case BOOLEAN -> "Z";
                case VOID -> "V";
            });
            return sb.toString();
        }
        
        @Override
        public Void visitFunctionDeclarationStmt(FunctionDeclaration stmt) {
            int access = Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC;
            List<Type> parameterTypes = stmt.parameters.stream().map(Pair::getRight).toList();
            MethodVisitor method = classWriter.visitMethod(access, stmt.identifier, getFunctionDescriptor(parameterTypes, stmt.type), null, null);
            
            method.visitCode(); // Start method body
            
            MethodBytecodeGenerator funcGenerator = new MethodBytecodeGenerator(classWriter, className, method, access, descriptor);
            
            // Add local variable indexes for each parameter
            int lvidx = 0;
            for (Pair<String, Type> parameter : stmt.parameters) {
                funcGenerator.lvTable.put(parameter.getLeft(), lvidx);
                lvidx += switch(parameter.getRight()) {
                    case NUMBER -> 2;
                    case STRING, BOOLEAN -> 1;
                    default -> throw new RuntimeException("Unable to register local variable index for parameter, unknown parameter type '" + parameter.getRight() + "'");
                };
            }
            
            // Generate method bytecode
            funcGenerator.generate(stmt.body);
            
            // Finish up
            method.visitInsn(Opcodes.RETURN);
            method.visitMaxs(0, 0);
            method.visitEnd();
            
            return null;
        }
        
        @Override
        public Void visitVariableDeclarationStmt(VariableDeclaration stmt) {
            stmt.value.accept(this);
            lvTable.put(stmt.identifier, lvs.newLocal(ASMUtil.tsTypeToAsmType(stmt.type)));
            method.visitVarInsn(switch(stmt.type) {
                case NUMBER -> Opcodes.DSTORE;
                case STRING -> Opcodes.ASTORE;
                case BOOLEAN -> Opcodes.ISTORE;
                default -> throw new RuntimeException("Unable to declare local variable, unknown parameter type '" + stmt.type + "'");
            }, lvTable.get(stmt.identifier));
            return null;
        }
        
        @Override
        public Void visitReturnStmt(Return stmt) {
            stmt.value.accept(this);
            switch(stmt.getType()) {
                case NUMBER -> method.visitInsn(Opcodes.DRETURN);
                case STRING -> method.visitInsn(Opcodes.ARETURN);
                case BOOLEAN -> method.visitInsn(Opcodes.IRETURN);
            }
            return null;
        }
        
        @Override
        public Void visitIfStmt(If stmt) {
            return null;
        }
        
        @Override
        public Void visitForStmt(For stmt) {
            return null;
        }
        
        @Override
        public Void visitWhileStmt(While stmt) {
            return null;
        }
        
        @Override
        public Void visitNoOpStmt(NoOp stmt) {
            return null;
        }
        
        @Override
        public Void visitBreakStmt(Break stmt) {
            return null;
        }
        
        @Override
        public Void visitContinueStmt(Continue stmt) {
            return null;
        }
    }
    
    private static class MethodExtractor extends ClassVisitor {
        
        private final String methodName;
        private String methodDescription;
        
        protected MethodExtractor(String methodName) {
            super(Opcodes.ASM9);
            this.methodName = methodName;
        }
        
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.equals(methodName))
                methodDescription = descriptor;
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}
