/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.tokenizer;

public class Char {
    private final char character;
    private final int index;
    private final int line;
    
    
    public Char(char character, int index, int line) {
        this.character = character;
        this.index = index;
        this.line = line;
    }
    
    public boolean is(char... tests) {
        for(char test : tests) {
            if(test == character && test != '\0') {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return Character.toString(character);
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
    
    public boolean isEOF() {
        return character == '\0';
    }
}
