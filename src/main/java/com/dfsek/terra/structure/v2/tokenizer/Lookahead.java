package com.dfsek.terra.structure.v2.tokenizer;

import org.polydev.gaea.util.GlueList;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class Lookahead {
    private final List<Char> buffer = new GlueList<>();
    private final Reader input;
    private int index = 0;
    private int line = 0;
    private boolean end = false;

    public Lookahead(Reader r) {
        this.input = r;
    }

    public Char current() {
        return next(0);
    }

    public Char consume() {
        Char consumed = current();
        consume(1);
        return consumed;
    }

    /**
     * Fetch and consume the next character.
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
}
