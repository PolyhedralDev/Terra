package com.dfsek.terra.lifecycle.asm;

import net.gudenau.minecraft.asm.api.v1.Identifier;
import net.gudenau.minecraft.asm.api.v1.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.io.IOException;

import com.dfsek.terra.lifecycle.util.ASMUtil;
import com.dfsek.terra.lifecycle.util.LoaderUtil;
import com.dfsek.terra.mod.util.FertilizableUtil;

import static org.objectweb.asm.Opcodes.ACC_STATIC;


public class FertilizableASM implements Transformer {

    private static String fertilizableClassName = LoaderUtil.INSTANCE.mapClassName("intermediary", "net.minecraft.class_2256");
    
    private static String fertilizableGrowMethodSignatureBase = String.format("(L%1$s;L%2$s;L%3$s;L%4$s;)",
                                                                          LoaderUtil.INSTANCE.mapClassName("intermediary", "net.minecraft.class_3218").replace(".", "/"),
                                                                          LoaderUtil.INSTANCE.mapClassName("intermediary", "net.minecraft.class_5819").replace(".", "/"),
                                                                          LoaderUtil.INSTANCE.mapClassName("intermediary", "net.minecraft.class_2338").replace(".", "/"),
                                                                          LoaderUtil.INSTANCE.mapClassName("intermediary", "net.minecraft.class_2680").replace(".", "/"));
    
    private static String fertilizableGrowMethodSignature = fertilizableGrowMethodSignatureBase + "V";
    
    private static String fertilizableGrowMethodName = LoaderUtil.INSTANCE.mapMethodName("intermediary", "net.minecraft.class_2256", "method_9652", "(Lnet/minecraft/class_3218;Lnet/minecraft/class_5819;Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;)V");
    
    @Override
    public Identifier getName() {
        return new Identifier("terra", "asm_test");
    }
    
    @Override
    public boolean handlesClass(String name, String transformedName) {
        return true;
    }
    
    @Override
    public boolean transform(ClassNode classNode, Flags flags) {
        try {
            if (ASMUtil.inheritsFrom(classNode, fertilizableClassName.replace(".", "/"))) {
                for (MethodNode method : classNode.methods) {
                    if (method.name.equals(fertilizableGrowMethodName)) {
                        if ((method.access & ACC_STATIC) == 0) {
                            if(method.desc.equals(fertilizableGrowMethodSignature)) {
                                InsnList list = new InsnList();
                                list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                                list.add(new VarInsnNode(Opcodes.ALOAD, 3));
                                list.add(new VarInsnNode(Opcodes.ALOAD, 4));
                                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, FertilizableUtil.class.getName().replace(".", "/"), "grow", fertilizableGrowMethodSignatureBase + "Z", false));
                                LabelNode jmp = new LabelNode();
                                list.add(new JumpInsnNode(Opcodes.IFNE, jmp));
                                list.add(new InsnNode(Opcodes.RETURN));
                                list.add(jmp);
                                method.instructions.insertBefore(method.instructions.getFirst(), list);
                                flags.requestFrames();
                                return true;
                            }
                        }
                    }
                }
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
