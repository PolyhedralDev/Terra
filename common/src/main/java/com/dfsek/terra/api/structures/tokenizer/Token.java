package com.dfsek.terra.api.structures.tokenizer;

public class Token {
    private final String content;
    private final Type type;

    public Token(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return type + ": '" + content + "'";
    }

    public enum Type {
        IDENTIFIER, NUMBER, STRING, BOOLEAN, BODY_BEGIN, BODY_END, STATEMENT_END, SEPARATOR
    }
}
