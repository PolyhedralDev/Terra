package com.dfsek.terra.addons.structure.structures.tokenizer;

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

    public boolean isWhitespace() {
        return Character.isWhitespace(character);
    }

    public boolean isNewLine() {
        return character == '\n';
    }

    public boolean isDigit() {
        return Character.isDigit(character);
    }

    public boolean is(char... tests) {
        for(char test : tests) {
            if(test == character && test != '\0') {
                return true;
            }
        }
        return false;
    }

    public boolean isEOF() {
        return character == '\0';
    }

    @Override
    public String toString() {
        return Character.toString(character);
    }
}
