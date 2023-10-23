/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.v2.lexer;

import java.util.Objects;


public class Char {
    private final char character;
    private final SourcePosition position;
    
    
    public Char(char character, SourcePosition position) {
        this.character = character;
        this.position = position;
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
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Char other = (Char) o;
        return character == other.character && Objects.equals(position, other.position);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(character, position);
    }
    
    public char getCharacter() {
        return character;
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
