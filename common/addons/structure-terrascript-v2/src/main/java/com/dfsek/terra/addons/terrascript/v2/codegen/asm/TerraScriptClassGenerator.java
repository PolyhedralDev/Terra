package com.dfsek.terra.addons.terrascript.v2.codegen.asm;

import com.dfsek.terra.addons.terrascript.v2.Type;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Assignment;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Binary;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Call;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Grouping;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Literal;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Unary;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Variable;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr.Void;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.Block;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.Break;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.Continue;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.Expression;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.For;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.FunctionDeclaration;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.If;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.NoOp;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.Return;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.VariableDeclaration;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt.While;

import com.dfsek.terra.addons.terrascript.v2.codegen.CodegenType;
import com.dfsek.terra.addons.terrascript.v2.codegen.CodegenType.InstructionType;
import com.dfsek.terra.addons.terrascript.v2.codegen.NativeFunction;
import com.dfsek.terra.addons.terrascript.v2.codegen.TerraScript;
import com.dfsek.terra.addons.terrascript.v2.exception.CompilerBugException;
import com.dfsek.terra.addons.terrascript.v2.util.ASMUtil;
import com.dfsek.terra.addons.terrascript.v2.parser.BinaryOperator;
import com.dfsek.terra.addons.terrascript.v2.parser.UnaryOperator;
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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dfsek.terra.addons.terrascript.v2.parser.BinaryOperator.*;
import static com.dfsek.terra.addons.terrascript.v2.parser.UnaryOperator.*;
import static com.dfsek.terra.addons.terrascript.v2.util.ASMUtil.dynamicName;

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
    
    private static class MethodBytecodeGenerator implements TypedStmt.Visitor<Void>, TypedExpr.Visitor<Void> {
        
        private final ClassWriter classWriter;
        
        private final String className;
        
        private final MethodVisitor method;
        
        private final String descriptor;
        
        private final LocalVariablesSorter lvs;
        
        private final Map<String, Integer> lvTable = new HashMap<>();
        
        private final Deque<Pair<Label, Label>> loopStack = new ArrayDeque<>();
        
        public MethodBytecodeGenerator(ClassWriter classWriter, String className, MethodVisitor method, int access, String descriptor) {
            this.classWriter = classWriter;
            this.className = className;
            this.method = method;
            this.descriptor = descriptor;
            this.lvs = new LocalVariablesSorter(access, descriptor, method);
        }
        
        public void generate(Block root) {
            this.visitBlockTypedStmt(root);
            
        }
        
        @Override
        public Void visitBinaryTypedExpr(Binary expr) {
            switch(expr.operator) {
                case EQUALS, NOT_EQUALS, BOOLEAN_AND, BOOLEAN_OR, GREATER, GREATER_EQUALS, LESS, LESS_EQUALS -> pushComparisonBool(expr);
                case ADD -> {
                    pushBinaryOperands(expr);
                    CodegenType codegenType = CodegenType.codegenType(expr.type);
                    if(codegenType.bytecodeType() == InstructionType.DOUBLE)
                        method.visitInsn(Opcodes.DADD);
                    else if (Objects.equals(codegenType.getDescriptor(), "Ljava/lang/String;"))
                        // TODO - Optimize string concatenation
                        method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
                    else throw new RuntimeException("Could not generate bytecode for ADD binary operator returning type " + expr.type);
                }
                case SUBTRACT -> binaryInsn(expr, Opcodes.DSUB);
                case MULTIPLY -> binaryInsn(expr, Opcodes.DMUL);
                case DIVIDE -> binaryInsn(expr, Opcodes.DDIV);
                default -> throw new RuntimeException("Unhandled binary operator " + expr.operator);
            }
            return null;
        }
        
        @Override
        public Void visitGroupingTypedExpr(Grouping expr) {
            expr.expression.accept(this);
            return null;
        }
        
        @Override
        public Void visitLiteralTypedExpr(Literal expr) {
            if(CodegenType.codegenType(expr.type) == CodegenType.BOOLEAN_PRIMITIVE)
                if ((boolean) expr.value) pushTrue(); else pushFalse();
            else method.visitLdcInsn(expr.value);
            return null;
        }
        
        @Override
        public Void visitUnaryTypedExpr(Unary expr) {
            expr.operand.accept(this);
            switch (expr.operator) {
                case NOT -> invertBool();
                case NEGATE -> method.visitInsn(Opcodes.DNEG);
            }
            return null;
        }
        
        @Override
        public Void visitCallTypedExpr(Call expr) {
            if (NativeFunction.BUILTIN_FUNCTIONS.containsKey(expr.identifier)) {
                NativeFunction function = NativeFunction.BUILTIN_FUNCTIONS.get(expr.identifier);
                function.pushInstance(method);
                expr.arguments.forEach(a -> a.accept(this));
                function.callMethod(method);
                return null;
            }
            expr.arguments.forEach(a -> a.accept(this));
            List<Type> parameters = expr.arguments.stream().map(e -> e.type).toList();
            method.visitMethodInsn(Opcodes.INVOKESTATIC, className, expr.scopedIdentifier, getFunctionDescriptor(parameters, expr.type), false);
            return null;
        }
        
        @Override
        public Void visitVariableTypedExpr(Variable expr) {
            Type varType = expr.type;
            method.visitVarInsn(CodegenType.codegenType(varType).bytecodeType().loadInsn(), lvTable.get(expr.identifier));
            return null;
        }
        
        @Override
        public Void visitAssignmentTypedExpr(Assignment expr) {
            expr.rValue.accept(this);
            Type type = expr.lValue.type;
            method.visitVarInsn(CodegenType.codegenType(type).bytecodeType().storeInsn(), lvTable.get(expr.lValue.identifier));
            return null;
        }
        
        @Override
        public Void visitVoidTypedExpr(Void expr) {
            return null;
        }
        
        @Override
        public Void visitExpressionTypedStmt(Expression stmt) {
            stmt.expression.accept(this);
            return null;
        }
        
        @Override
        public Void visitBlockTypedStmt(Block stmt) {
            stmt.statements.forEach(s -> s.accept(this));
            return null;
        }
        
        /**
         * Writes function as a private static method of the current class
         * @param stmt
         * @return
         */
        @Override
        public Void visitFunctionDeclarationTypedStmt(FunctionDeclaration stmt) {
            List<Type> parameterTypes = stmt.parameters.stream().map(Pair::getRight).toList();
            
            int access = Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC;
            
            MethodVisitor method = classWriter.visitMethod(access, stmt.scopedIdentifier, getFunctionDescriptor(parameterTypes, stmt.returnType), null, null);
            
            method.visitCode(); // Start method body
            
            MethodBytecodeGenerator funcGenerator = new MethodBytecodeGenerator(classWriter, className, method, access, descriptor);
            
            // Add local variable indexes for each parameter
            int lvidx = 0;
            for (Pair<String, Type> parameter : stmt.parameters) {
                funcGenerator.lvTable.put(parameter.getLeft(), lvidx);
                lvidx += CodegenType.codegenType(parameter.getRight()).bytecodeType().slotSize(); // Increment by how many slots data type takes
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
        public Void visitVariableDeclarationTypedStmt(VariableDeclaration stmt) {
            stmt.value.accept(this);
            lvTable.put(stmt.identifier, lvs.newLocal(ASMUtil.tsTypeToAsmType(stmt.type)));
            method.visitVarInsn(CodegenType.codegenType(stmt.type).bytecodeType().storeInsn(), lvTable.get(stmt.identifier));
            return null;
        }
        
        @Override
        public Void visitReturnTypedStmt(Return stmt) {
            stmt.value.accept(this);
            method.visitInsn(CodegenType.codegenType(stmt.value.type).bytecodeType().returnInsn());
            return null;
        }
        
        @Override
        public Void visitIfTypedStmt(If stmt) {
            Label endIf = new Label();
            conditionalStmt(stmt.condition, stmt.trueBody, endIf);
            for(Pair<TypedExpr, Block> elseIfClause : stmt.elseIfClauses) {
                conditionalStmt(elseIfClause.getLeft(), elseIfClause.getRight(), endIf);
            }
            stmt.elseBody.ifPresent(b -> b.accept(this));
            label(endIf);
            return null;
        }
        
        @Override
        public Void visitForTypedStmt(For stmt) {
            Label loopStart = new Label();
            Label loopBody = new Label();
            Label loopEnd = new Label();
            
            stmt.initializer.accept(this);
            jump(loopBody); // Skip over incrementer on first loop
            
            label(loopStart);
            stmt.incrementer.accept(this);
            label(loopBody);
            loopStack.push(Pair.of(loopStart, loopEnd));
            conditionalStmt(stmt.condition, stmt.body, loopStart);
            loopStack.pop();
            label(loopEnd);
            return null;
        }
        
        @Override
        public Void visitWhileTypedStmt(While stmt) {
            Label loopStart = new Label();
            Label loopEnd = new Label();
            
            label(loopStart);
            loopStack.push(Pair.of(loopStart, loopEnd));
            conditionalStmt(stmt.condition, stmt.body, loopStart);
            loopStack.pop();
            label(loopEnd);
            return null;
        }
        
        @Override
        public Void visitNoOpTypedStmt(NoOp stmt) {
            return null;
        }
        
        @Override
        public Void visitBreakTypedStmt(Break stmt) {
            jump(loopStack.getFirst().getRight());
            return null;
        }
        
        @Override
        public Void visitContinueTypedStmt(Continue stmt) {
            jump(loopStack.getFirst().getLeft());
            return null;
        }
        
        private boolean binaryOperandsSameType(Type type, Binary expr) {
            return exprTypesEqual(type, expr.left, expr.right);
        }
        
        private static boolean exprTypesEqual(Type type, TypedExpr... exprs) {
            for(TypedExpr expr : exprs) {
                if (expr.type != type) return false;
            }
            return true;
        }
        
        /**
         * Inverts a boolean on the stack
         */
        private void invertBool() {
            Label invertToFalse = new Label();
            Label finished = new Label();
            jumpIf(OpcodeAlias.BOOL_TRUE, invertToFalse);
            
            pushFalse();
            jump(finished);
            
            label(invertToFalse);
            pushFalse();
            
            label(finished);
        }
        
        private void pushBinaryOperands(Binary expr) {
            expr.left.accept(this);
            expr.right.accept(this);
        }
        
        private void binaryInsn(Binary expr, int insn) {
            pushBinaryOperands(expr);
            method.visitInsn(insn);
        }
        
        /**
         * Pushes boolean on to the stack based on comparison result
         * @param condition
         */
        private void pushComparisonBool(TypedExpr condition) {
            Label trueFinished = new Label();
            conditionalRunnable(condition, this::pushTrue, trueFinished);
            pushFalse();
            label(trueFinished);
        }
        
        /**
         * Executes a statement then jumps to the exit label if the condition is true, jumps over the statement if false
         * @param condition
         * @param stmt
         * @param exit
         */
        private void conditionalStmt(TypedExpr condition, TypedStmt stmt, Label exit) {
            conditionalRunnable(condition, () -> stmt.accept(this), exit);
        }
        
        private void pushTrue() {
            method.visitInsn(Opcodes.ICONST_1);
        }
        
        private void pushFalse() {
            method.visitInsn(Opcodes.ICONST_0);
        }
        
        private void jumpIf(int opcode, Label label) {
            method.visitJumpInsn(opcode, label);
        }
        
        private void jump(Label label) {
            method.visitJumpInsn(Opcodes.GOTO, label);
        }
        
        private void label(Label label) {
            method.visitLabel(label);
        }
        
        private void conditionalRunnable(TypedExpr condition, Runnable trueBlock, Label trueFinished) {
            Label exit = new Label(); // If the first conditional is false, jump over statement and don't execute it
            if (condition instanceof Binary binaryCondition) {
                switch(binaryCondition.operator) {
                    case BOOLEAN_AND -> {
                        // Operands assumed booleans
                        binaryCondition.left.accept(this);
                        jumpIf(OpcodeAlias.BOOL_FALSE, exit); // If left is false, short circuit, don't evaluate right
                        binaryCondition.right.accept(this);
                        jumpIf(OpcodeAlias.BOOL_FALSE, exit);
                    }
                    case BOOLEAN_OR -> {
                        Label skipRight = new Label();
                        // Operands assumed booleans
                        binaryCondition.left.accept(this);
                        jumpIf(OpcodeAlias.BOOL_TRUE, skipRight);
                        binaryCondition.right.accept(this);
                        jumpIf(OpcodeAlias.BOOL_FALSE, exit);
                        label(skipRight);
                    }
                    case EQUALS -> {
                        if (binaryOperandsSameType(Type.BOOLEAN, binaryCondition)) { // Operands assumed integers
                            pushBinaryOperands(binaryCondition);
                            jumpIf(OpcodeAlias.INTEGERS_NOT_EQUAL, exit);
                            
                        } else if (binaryOperandsSameType(Type.NUMBER, binaryCondition)) { // Operands assumed doubles
                            binaryInsn(binaryCondition, Opcodes.DCMPG);
                            jumpIf(OpcodeAlias.CMP_NOT_EQUALS, exit);
                            
                        } else if (binaryOperandsSameType(Type.STRING, binaryCondition)) {
                            pushBinaryOperands(binaryCondition);
                            method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                            jumpIf(OpcodeAlias.BOOL_FALSE, exit);
                        } else throw new CompilerBugException();
                    }
                    case NOT_EQUALS -> {
                        if (binaryOperandsSameType(Type.BOOLEAN, binaryCondition)) { // Operands assumed integers
                            pushBinaryOperands(binaryCondition);
                            jumpIf(OpcodeAlias.INTEGERS_EQUAL, exit);
                            
                        } else if (binaryOperandsSameType(Type.NUMBER, binaryCondition)) { // Operands assumed doubles
                            binaryInsn(binaryCondition, Opcodes.DCMPG);
                            jumpIf(OpcodeAlias.CMP_EQUALS, exit);
                            
                        } else if (binaryOperandsSameType(Type.STRING, binaryCondition)) { // Operands assumed references
                            pushBinaryOperands(binaryCondition);
                            method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                            invertBool();
                            jumpIf(OpcodeAlias.CMP_EQUALS, exit);
                        } else throw new CompilerBugException();
                    }
                    case GREATER, GREATER_EQUALS, LESS, LESS_EQUALS -> {
                        // Left and right assumed double
                        binaryInsn(binaryCondition, switch(binaryCondition.operator) {
                            case GREATER, GREATER_EQUALS -> Opcodes.DCMPL;
                            case LESS, LESS_EQUALS -> Opcodes.DCMPG;
                            default -> throw new IllegalStateException();
                        });
                        
                        jumpIf(switch(binaryCondition.operator) {
                            case GREATER -> OpcodeAlias.CMP_LESS_EQUALS;
                            case GREATER_EQUALS -> OpcodeAlias.CMP_LESS_THAN;
                            case LESS -> OpcodeAlias.CMP_GREATER_EQUALS;
                            case LESS_EQUALS -> OpcodeAlias.CMP_GREATER_THAN;
                            default -> throw new IllegalStateException();
                        }, exit);
                    }
                    default -> throw new CompilerBugException();
                }
            } else {
                // Assume condition returns bool
                condition.accept(this);
                jumpIf(OpcodeAlias.BOOL_FALSE, exit);
            }
            trueBlock.run();
            jump(trueFinished); // Jump to end of statement after execution
            label(exit);
        }
        
        private String getFunctionDescriptor(List<Type> parameters, Type returnType) {
            StringBuilder sb = new StringBuilder().append("(");
            parameters.stream().map(parameter -> CodegenType.codegenType(parameter).getDescriptor()).forEach(sb::append);
            sb.append(")");
            sb.append(CodegenType.codegenType(returnType).getDescriptor());
            return sb.toString();
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
