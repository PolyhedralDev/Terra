package com.dfsek.terra.structure.v2.tokenizer;

import java.io.StringReader;

public class Tokenizer {
    private final Lookahead reader;

    public Tokenizer(String data) {
        reader = new Lookahead(new StringReader(data));

    }
}
