package com.dfsek.terra.addons.terrascript.v2.codegen.asm;

import org.objectweb.asm.Opcodes;


public class OpcodeAlias {
    public static int CMP_GREATER_THAN = Opcodes.IFGT;
    public static int CMP_GREATER_EQUALS = Opcodes.IFGE;
    public static int CMP_LESS_THAN = Opcodes.IFLT;
    public static int CMP_LESS_EQUALS = Opcodes.IFLE;
    public static int CMP_EQUALS = Opcodes.IFEQ;
    public static int CMP_NOT_EQUALS = Opcodes.IFNE;
    public static int BOOL_FALSE = Opcodes.IFEQ;
    public static int BOOL_TRUE = Opcodes.IFNE;
    public static int INTEGERS_EQUAL = Opcodes.IF_ICMPEQ;
    public static int INTEGERS_NOT_EQUAL = Opcodes.IF_ICMPNE;
}
