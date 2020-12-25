package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.tokenizer.Token;
import com.dfsek.terra.api.util.GlueList;

import java.util.List;

/**
 * Data structure to hold tokens, where items are inserted at the top and removed from the bottom.
 */
public class TokenHolder {
    private final List<Token> tokens = new GlueList<>();

    /**
     * Add a token to the top of the stack.
     *
     * @param token Token to add
     */
    public void add(Token token) {
        tokens.add(token);
    }

    /**
     * Get the token at the bottom of the stack.
     *
     * @return First token
     * @throws ParseException If stack is empty
     */
    public Token get() throws ParseException {
        if(!hasNext()) throw new ParseException("Unexpected end of input");
        return tokens.get(0);
    }

    /**
     * Consume (get and remove) the token at the bottom of the stack.
     *
     * @return First token
     * @throws ParseException If stack is empty
     */
    public Token consume() throws ParseException {
        if(!hasNext()) throw new ParseException("Unexpected end of input");
        return tokens.remove(0);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public boolean hasNext() {
        return tokens.size() > 0;
    }
}
