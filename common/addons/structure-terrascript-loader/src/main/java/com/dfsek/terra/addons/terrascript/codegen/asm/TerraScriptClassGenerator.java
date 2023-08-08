package com.dfsek.terra.addons.terrascript.codegen.asm;

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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.dfsek.terra.addons.terrascript.util.ASMUtil.dynamicName;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.IOR;
import static org.objectweb.asm.Opcodes.RETURN;


public class TerraScriptClassGenerator {
    
    private static final Class<?> TARGET_CLASS = TerraScript.class;
    
    private static final boolean DUMP = true;
    
    private int generationCount = 0;
    
    private final String debugPath;
    
    public TerraScriptClassGenerator(String debugPath) {
        this.debugPath = debugPath;
    }
    
    public TerraScript generate(Block root) throws IOException {
        String targetClassName = dynamicName(TARGET_CLASS);
        String generatedClassName = targetClassName + "_GENERATED_" + generationCount;
        
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        
        // Create class
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, generatedClassName, null, "java/lang/Object", new String[]{ targetClassName });
       
        // Generate constructor method
        MethodVisitor constructor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(ALOAD, 0); // Put this reference on stack
        constructor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(RETURN); // Void return
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();
        
        // Generate execute method
        String methodName = "execute";
        // Extract method description
        MethodExtractor extractor = new MethodExtractor(methodName);
        new ClassReader(targetClassName).accept(extractor, 0);
        String description = extractor.methodDescription;
        MethodVisitor execute = classWriter.visitMethod(Opcodes.ACC_PUBLIC, methodName, description, null, null);
        execute.visitCode(); // Start method body
        
        execute.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        new BytecodeGenerator(execute).visitBlockStmt(root);
        execute.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
        
        execute.visitInsn(RETURN);
        execute.visitMaxs(0, 0);
        execute.visitEnd();
        
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
    
    private static class BytecodeGenerator implements Stmt.Visitor<Void>, Expr.Visitor<Void> {
        
        private final MethodVisitor method;
        
        public BytecodeGenerator(MethodVisitor method) {
            this.method = method;
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
                case ADD -> method.visitInsn(Opcodes.DADD);
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
            return null;
        }
        
        @Override
        public Void visitVariableExpr(Variable expr) {
            return null;
        }
        
        @Override
        public Void visitAssignmentExpr(Assignment expr) {
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
        
        @Override
        public Void visitFunctionDeclarationStmt(FunctionDeclaration stmt) {
            return null;
        }
        
        @Override
        public Void visitVariableDeclarationStmt(VariableDeclaration stmt) {
            return null;
        }
        
        @Override
        public Void visitReturnStmt(Return stmt) {
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
