package com.dfsek.terra.api.world.attribute;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum Operation implements StringIdentifiable {
    OVERRIDE("override"),
    ALPHA_BLEND("alpha_blend"),
    ADD("add"),
    SUBTRACT("subtract"),
    MULTIPLY("multiply"),
    BLEND_TO_GRAY("blend_to_gray"),
    MINIMUM("minimum"),
    MAXIMUM("maximum"),
    AND("and"),
    NAND("nand"),
    OR("or"),
    NOR("nor"),
    XOR("xor"),
    XNOR("xnor"),
    APPEND("append"),
    OVERLAY("overlay");

    private final String id;

    Operation(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }

    /**
     * @throws IllegalArgumentException if no operation has this {@link #getID() ID}
     */
    public static Operation parse(String name) {
        for(Operation value : values()) {
            if(value.id.equals(name)) return value;
        }
        throw new IllegalArgumentException("unknown modifier \"" + name + "\"");
    }
}
