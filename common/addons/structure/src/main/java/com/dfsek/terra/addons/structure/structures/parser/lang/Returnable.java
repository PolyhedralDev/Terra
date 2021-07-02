package com.dfsek.terra.addons.structure.structures.parser.lang;

public interface Returnable<T> extends Item<T> {
    ReturnType returnType();

    enum ReturnType {
        NUMBER(true), STRING(true), BOOLEAN(false), VOID(false), OBJECT(false);

        private final boolean comparable;

        ReturnType(boolean comparable) {
            this.comparable = comparable;
        }

        public boolean isComparable() {
            return comparable;
        }
    }
}
