/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package lexer;

import org.junit.jupiter.api.Test;

import com.dfsek.terra.addons.terrascript.v2.lexer.Char;
import com.dfsek.terra.addons.terrascript.v2.lexer.LookaheadStream;
import com.dfsek.terra.addons.terrascript.v2.lexer.SourcePosition;

import static org.junit.jupiter.api.Assertions.*;


public class LookaheadStreamTest {
    @Test
    public void lookahead() {
        String testString = "Test string...\nNew line";
        
        LookaheadStream lookahead = new LookaheadStream(testString);
        
        Char first = new Char('T', new SourcePosition(1, 1));
        Char second = new Char('e', new SourcePosition(1, 2));
        Char third = new Char('s', new SourcePosition(1, 3));
        Char space = new Char(' ', new SourcePosition(1, 5));
        Char newline = new Char('\n', new SourcePosition(1, 15));
        Char lineTwoColOne = new Char('N', new SourcePosition(2, 1));
        String lineTwo = "New line";
        
        assertTrue(lookahead.matchesString("Test", false));
        assertTrue(lookahead.matchesString(testString, false));
        assertFalse(lookahead.matchesString(testString + "asdf", false));
        assertFalse(lookahead.matchesString("Foo", false));
        
        assertEquals(first, lookahead.current());
        assertEquals(first, lookahead.current());
        
        assertEquals(new SourcePosition(1, 1), lookahead.getPosition());
        assertEquals(new SourcePosition(1, 1), lookahead.getPosition());
        
        assertEquals(second, lookahead.peek());
        assertEquals(second, lookahead.peek());
        
        assertEquals(first, lookahead.consume());
        
        assertFalse(lookahead.matchesString(testString, false));
        
        assertEquals(second, lookahead.current());
        
        assertEquals(second, lookahead.consume());
        
        assertEquals(third, lookahead.current());
        
        assertTrue(lookahead.matchesString("st", true));
        
        assertEquals(space, lookahead.current());
        
        assertEquals(space, lookahead.consume());
        
        assertTrue(lookahead.matchesString("string...", false));
        assertTrue(lookahead.matchesString("string...", true));
        assertFalse(lookahead.matchesString("string...", false));
        
        assertEquals(newline, lookahead.current());
        assertEquals(newline, lookahead.consume());
        
        assertEquals(lineTwoColOne, lookahead.current());
        
        assertTrue(lookahead.matchesString(lineTwo, false));
        assertFalse(lookahead.matchesString(lineTwo + "asdf", false));
        assertTrue(lookahead.matchesString(lineTwo, true));
        
        assertEquals(new SourcePosition(2, 8), lookahead.getPosition());
        
        assertDoesNotThrow(lookahead::consume);
        assertEquals(new SourcePosition(2, 8), lookahead.getPosition());
        
        assertDoesNotThrow(lookahead::consume);
        assertEquals(new SourcePosition(2, 8), lookahead.getPosition());
    }
}
