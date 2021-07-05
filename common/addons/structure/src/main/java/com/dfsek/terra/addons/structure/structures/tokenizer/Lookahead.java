package com.dfsek.terra.addons.structure.structures.tokenizer;

import com.dfsek.terra.api.util.GlueList;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Stream-like data structure that allows viewing future elements without consuming current.
 */
public class Lookahead {
    private final List<Char> buffer = new GlueList<>();
    private final Reader input;
    private int index = 0;
    private int line = 0;
    private boolean end = false;

    public Lookahead(Reader r) {
        this.input = r;
    }

    /**
     * Get the current character without consuming it.
     *
     * @return current character
     */
    public Char current() {
        return next(0);
    }


    /**
     * Consume and return one character.
     *
     * @return Character that was consumed.
     */
    public Char consume() {
        Char consumed = current();
        consume(1);
        return consumed;
    }

    /**
     * Fetch the next character.
     *
     * @return Next character
     */
    private Char fetch() {
        try {
            int c = input.read();
            if(c == -1) return null;
            if(c == '\n') {
                line++;
                index = 0;
            }
            index++;
            return new Char((char) c, line, index);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetch a future character without consuming it.
     *
     * @param ahead Distance ahead to peek
     * @return Character
     */
    public Char next(int ahead) {
        if(ahead < 0) throw new IllegalArgumentException();

        while(buffer.size() <= ahead && !end) {
            Char item = fetch();
            if(item != null) {
                buffer.add(item);
            } else end = true;
        }

        if(ahead >= buffer.size()) {
            return null;
        } else return buffer.get(ahead);
    }

    public int getLine() {
        return line;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Consume an amount of characters
     *
     * @param amount Number of characters to consume
     */
    public void consume(int amount) {
        if(amount < 0) throw new IllegalArgumentException();
        while(amount-- > 0) {
            if(!buffer.isEmpty()) buffer.remove(0); // Remove top item from buffer.
            else {
                if(end) return;
                Char item = fetch();
                if(item == null) end = true;
            }
        }
    }

    public boolean matches(String check, boolean consume) {
        if(check == null) return false;

        for(int i = 0; i < check.length(); i++) {
            if(!next(i).is(check.charAt(i))) return false;
        }

        if(consume) consume(check.length()); // Consume string
        return true;
    }
}