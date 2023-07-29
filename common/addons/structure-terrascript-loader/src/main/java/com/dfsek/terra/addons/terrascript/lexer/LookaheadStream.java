package com.dfsek.terra.addons.terrascript.lexer;



public class LookaheadStream {
    
    private final String source;
    
    private int index;
    
    private SourcePosition position = new SourcePosition(1, 1);
    
    public LookaheadStream(String source) {
        this.source = source;
    }
    
    /**
     * Get the current character without consuming it.
     *
     * @return current character
     */
    public Char current() {
        return new Char(source.charAt(index), position);
    }
    
    /**
     * Consume and return one character.
     *
     * @return Character that was consumed.
     */
    public Char consume() {
        Char consumed = current();
        incrementIndex(1);
        return consumed;
    }
    
    /**
     * @return The next character in sequence.
     */
    public Char peek() {
        int index = this.index + 1;
        if (index + 1 >= source.length()) return null;
        return new Char(source.charAt(index), getPositionAfter(1));
    }
    
    /**
     * Determines if the contained sequence of characters matches the string
     *
     * @param check Input string to check against
     * @param consumeIfMatched Whether to consume the string if there is a match
     * @return If the string matches
     */
    public boolean matchesString(String check, boolean consumeIfMatched) {
        boolean matches = check.equals(source.substring(index, Math.min(index + check.length(), source.length())));
        if (matches && consumeIfMatched) incrementIndex(check.length());
        return matches;
    }
    
    /**
     * @return Current position within the source file
     */
    public SourcePosition getPosition() {
        return position;
    }
    
    private void incrementIndex(int amount) {
        position = getPositionAfter(amount);
        index = Math.min(index + amount, source.length() - 1);
    }
    
    private SourcePosition getPositionAfter(int chars) {
        if (chars < 0) throw new IllegalArgumentException("Negative values are not allowed");
        int line = position.line();
        int column = position.column();
        for (int i = index; i < Math.min(index + chars, source.length() - 1); i++) {
            if (source.charAt(i) == '\n') {
                line++;
                column = 0;
            }
            column++;
        }
        return new SourcePosition(line, column);
    }
}
