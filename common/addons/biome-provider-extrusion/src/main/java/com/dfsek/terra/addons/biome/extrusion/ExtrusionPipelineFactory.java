package com.dfsek.terra.addons.biome.extrusion;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.world.biome.Biome;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.SWAP;
import static org.objectweb.asm.Opcodes.V1_8;


public class ExtrusionPipelineFactory {
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    // Type Descriptors
    private static final String EXTRUSION_TYPE = Type.getInternalName(Extrusion.class);
    private static final String EXTRUSION_DESC = Type.getDescriptor(Extrusion.class);
    private static final String BIOME_DESC = Type.getDescriptor(Biome.class);
    private static final String PIPELINE_INTERFACE = Type.getInternalName(ExtrusionPipeline.class);

    // Method Signature: (Biome, int, int, int, long) -> Biome
    private static final String EXTRUDE_SIG = "(" + BIOME_DESC + "IIIJ)" + BIOME_DESC;

    public static ExtrusionPipeline create(List<Extrusion> extrusions) {
        // Optimization: If empty, return identity
        if(extrusions.isEmpty()) {
            return (original, x, y, z, seed) -> original;
        }

        String className = "com/dfsek/terra/addons/biome/extrusion/GeneratedExtrusionPipeline_" + ID_COUNTER.getAndIncrement();

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        // 1. Define Class
        cw.visit(V1_8, ACC_PUBLIC | ACC_FINAL, className, null, "java/lang/Object", new String[]{ PIPELINE_INTERFACE });

        // 2. Define Fields (e0, e1, e2...)
        for(int i = 0; i < extrusions.size(); i++) {
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL, "e" + i, EXTRUSION_DESC, null, null);
            fv.visitEnd();
        }

        // 3. Generate Constructor(Extrusion[])
        generateConstructor(cw, className, extrusions.size());

        // 4. Generate extrude() method
        generateExtrudeMethod(cw, className, extrusions.size());

        cw.visitEnd();

        // 5. Load and Instantiate
        byte[] bytecode = cw.toByteArray();
        try {
            Class<?> generatedClass = new PipelineClassLoader(ExtrusionPipelineFactory.class.getClassLoader())
                .defineClass(className.replace('/', '.'), bytecode);

            return (ExtrusionPipeline) generatedClass.getConstructor(Extrusion[].class)
                .newInstance((Object) extrusions.toArray(new Extrusion[0]));
        } catch(Exception e) {
            throw new RuntimeException("Failed to generate ExtrusionPipeline", e);
        }
    }

    private static void generateConstructor(ClassWriter cw, String className, int count) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([L" + EXTRUSION_TYPE + ";)V", null, null);
        mv.visitCode();

        // super()
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        // Assign array elements to fields
        for(int i = 0; i < count; i++) {
            mv.visitVarInsn(ALOAD, 0);          // Load this
            mv.visitVarInsn(ALOAD, 1);          // Load array argument
            mv.visitIntInsn(SIPUSH, i);         // Load index
            mv.visitInsn(AALOAD);               // Load array[i]
            mv.visitFieldInsn(PUTFIELD, className, "e" + i, EXTRUSION_DESC);
        }

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0); // Computed automatically
        mv.visitEnd();
    }

    private static void generateExtrudeMethod(ClassWriter cw, String className, int count) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "extrude", EXTRUDE_SIG, null, null);
        mv.visitCode();

        // Helper var indices:
        // 0: this
        // 1: Biome original (We will update this or chain it on stack)
        // 2: int x
        // 3: int y
        // 4: int z
        // 5: long seed

        mv.visitVarInsn(ALOAD, 1); // Load 'original' Biome onto stack initially

        for(int i = 0; i < count; i++) {
            // Stack contains: [CurrentBiome]

            mv.visitVarInsn(ALOAD, 0); // Load 'this'
            mv.visitFieldInsn(GETFIELD, className, "e" + i, EXTRUSION_DESC); // Load Extrusion field

            // Stack: [CurrentBiome, Extrusion]
            // We need: [Extrusion, CurrentBiome, x, y, z, seed]

            mv.visitInsn(SWAP); // Swap to get [Extrusion, CurrentBiome]

            mv.visitVarInsn(ILOAD, 2); // x
            mv.visitVarInsn(ILOAD, 3); // y
            mv.visitVarInsn(ILOAD, 4); // z
            mv.visitVarInsn(LLOAD, 5); // seed

            // Invoke Extrusion.extrude(Biome, x, y, z, seed)
            mv.visitMethodInsn(INVOKEINTERFACE, EXTRUSION_TYPE, "extrude", EXTRUDE_SIG, true);

            // Stack now contains: [NewBiome]
            // Loop continues using this result as input for the next one
        }

        mv.visitInsn(ARETURN); // Return the final Biome
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    // Custom ClassLoader to inject the bytes
    private static class PipelineClassLoader extends ClassLoader {
        public PipelineClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}