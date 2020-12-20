package com.dfsek.terra.api.structures.tokenizer;

public class Token {
    private final String content;
    private final Type type;
    private final Position start;

    public Token(String content, Type type, Position start) {
        this.content = content;
        this.type = type;
        this.start = start;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Position getStart() {
        return start;
    }

    @Override
    public String toString() {
        return type + ": '" + content + "'";
    }

    public enum Type {
        IDENTIFIER, NUMBER, STRING, BOOLEAN, BODY_BEGIN, BODY_END, STATEMENT_END, SEPARATOR, BLOCK_BEGIN, BLOCK_END
    }
}
