package com.dfsek.terra.mod.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.WeakHashMap;

import static org.objectweb.asm.ClassReader.SKIP_CODE;
import static org.objectweb.asm.ClassReader.SKIP_DEBUG;
import static org.objectweb.asm.ClassReader.SKIP_FRAMES;


public class ASMUtil {
    static final ThreadLocal<WeakHashMap<String, WeakHashMap<String, Boolean>>> INHERITANCE_CACHE = new ThreadLocal<>();
    
    public static boolean inheritsFrom(ClassNode classNode, String interfaceName) throws IOException {
        if (INHERITANCE_CACHE.get() == null) {
            INHERITANCE_CACHE.set(new WeakHashMap<>());
        }
        return inheritsFromInternal(classNode, interfaceName);
    }
    
    protected static boolean inheritsFromInternal(ClassNode classNode, String interfaceName) throws IOException {
        if (INHERITANCE_CACHE.get().containsKey(classNode.name)) {
            if (INHERITANCE_CACHE.get().get(classNode.name).containsKey(interfaceName)) {
                return INHERITANCE_CACHE.get().get(classNode.name).get(interfaceName);
            }
        }
        for (String parent : classNode.interfaces) {
            if (checkClass(parent, interfaceName)) {
                return true;
            }
        }
        if (checkClass(classNode.superName, interfaceName)) {
            return true;
        }
        INHERITANCE_CACHE.get().getOrDefault(classNode.name, new WeakHashMap<>()).put(interfaceName, false);
        return false;
    }
    
    protected static boolean checkClass(String name, String interfaceName) throws IOException {
        if (name.startsWith("java/")) {
            return false;
        }
        
        boolean isClass = name.equals(interfaceName);
        INHERITANCE_CACHE.get().getOrDefault(name, new WeakHashMap<>()).put(interfaceName, isClass);
        if (isClass) {
            return true;
        }
        ClassNode node = new ClassNode();
        new ClassReader(name).accept(node, SKIP_CODE | SKIP_DEBUG | SKIP_FRAMES);
        return inheritsFromInternal(node, interfaceName);
    }
}
