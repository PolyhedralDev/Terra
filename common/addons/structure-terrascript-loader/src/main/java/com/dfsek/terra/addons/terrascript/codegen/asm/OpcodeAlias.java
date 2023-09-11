package com.dfsek.terra.addons.terrascript.codegen.asm;

import org.objectweb.asm.Opcodes;


public enum OpcodeAlias {
    CMP_GREATER_THAN(Opcodes.IFGT),
    CMP_GREATER_EQUALS(Opcodes.IFGE),
    CMP_LESS_THAN(Opcodes.IFLT),
    CMP_LESS_EQUALS(Opcodes.IFLE),
    CMP_EQUALS(Opcodes.IFEQ),
    CMP_NOT_EQUALS(Opcodes.IFNE),
    BOOL_FALSE(Opcodes.IFEQ),
    BOOL_TRUE(Opcodes.IFNE),
    INTEGERS_EQUAL(Opcodes.IF_ICMPEQ),
    INTEGERS_NOT_EQUAL(Opcodes.IF_ICMPNE),
    ;
    
    public final int opcode;
    
    OpcodeAlias(int opcode) { this.opcode = opcode; }
}
