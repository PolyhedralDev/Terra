package com.dfsek.terra.structure.v2.tokenizer;

public class Char {
    private final char character;
    private final int index;
    private final int line;


    public Char(char character, int index, int line) {
        this.character = character;
        this.index = index;
        this.line = line;
    }

    public char getCharacter() {
        return character;
    }

    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }

    public boolean is(char... tests) {
        for(char test : tests) {
            if(test == character && test != '\0') {
                return true;
            }
        }
        return false;
    }
}
