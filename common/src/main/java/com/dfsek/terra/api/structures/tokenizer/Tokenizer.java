package com.dfsek.terra.api.structures.tokenizer;

import com.dfsek.terra.api.structures.tokenizer.exceptions.EOFException;
import com.dfsek.terra.api.structures.tokenizer.exceptions.FormatException;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import com.google.common.collect.Sets;

import java.io.StringReader;
import java.util.Set;

public class Tokenizer {
    private final Lookahead reader;

    private final Set<Character> syntaxSignificant = Sets.newHashSet(';', '(', ')', '"', ',', '\\', '=', '{', '}', '+'); // Reserved chars
    private final Set<String> keywords = Sets.newHashSet("if", "return");


    public Tokenizer(String data) {
        reader = new Lookahead(new StringReader(data + '\0'));
    }

    public boolean hasNext() {
        while(!reader.current().isEOF() && reader.current().isWhitespace()) reader.consume(); // Consume whitespace.
        return !reader.current().isEOF();
    }

    public Token fetch() throws TokenizerException {
        while(!reader.current().isEOF() && reader.current().isWhitespace()) reader.consume();
        if(reader.current().isEOF()) return null; // EOF

        if(reader.matches("//", true)) skipLine(); // Skip line if comment

        if(reader.matches("/*", true)) skipTo("*/"); // Skip multi line comment

        if(reader.matches("true", true))
            return new Token("true", Token.Type.BOOLEAN, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("false", true))
            return new Token("false", Token.Type.BOOLEAN, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("==", true))
            return new Token("==", Token.Type.BOOLEAN_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("!=", true))
            return new Token("!=", Token.Type.BOOLEAN_OPERATOR, new Position(reader.getLine(), reader.getIndex()));

        if(isNumberStart()) {
            StringBuilder num = new StringBuilder();
            while(!reader.current().isEOF() && isNumberLike()) {
                num.append(reader.consume());
            }
            return new Token(num.toString(), Token.Type.NUMBER, new Position(reader.getLine(), reader.getIndex()));
        }

        if(reader.current().is('"')) {
            reader.consume(); // Consume first quote
            StringBuilder string = new StringBuilder();
            boolean ignoreNext = false;
            while((!reader.current().is('"')) || ignoreNext) {
                if(reader.current().is('\\') && !ignoreNext) {
                    ignoreNext = true;
                    reader.consume();
                    continue;
                } else ignoreNext = false;
                if(reader.current().isEOF())
                    throw new FormatException("No end of string literal found. " + reader.getLine() + ":" + reader.getIndex());
                string.append(reader.consume());
            }
            reader.consume(); // Consume last quote

            return new Token(string.toString(), Token.Type.STRING, new Position(reader.getLine(), reader.getIndex()));
        }

        if(reader.current().is('('))
            return new Token(reader.consume().toString(), Token.Type.BODY_BEGIN, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is(')'))
            return new Token(reader.consume().toString(), Token.Type.BODY_END, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is(';'))
            return new Token(reader.consume().toString(), Token.Type.STATEMENT_END, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is(','))
            return new Token(reader.consume().toString(), Token.Type.SEPARATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('{'))
            return new Token(reader.consume().toString(), Token.Type.BLOCK_BEGIN, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('}'))
            return new Token(reader.consume().toString(), Token.Type.BLOCK_END, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('='))
            return new Token(reader.consume().toString(), Token.Type.ASSIGNMENT, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('+'))
            return new Token(reader.consume().toString(), Token.Type.ADDITION_OPERATOR, new Position(reader.getLine(), reader.getIndex()));

        StringBuilder token = new StringBuilder();
        while(!reader.current().isEOF() && !isSyntaxSignificant(reader.current().getCharacter())) {
            Char c = reader.consume();
            if(!c.isWhitespace()) token.append(c);
        }

        String tokenString = token.toString();


        return new Token(tokenString, keywords.contains(tokenString) ? Token.Type.KEYWORD : Token.Type.IDENTIFIER, new Position(reader.getLine(), reader.getIndex()));
    }

    private boolean isNumberLike() {
        return reader.current().isDigit()
                || reader.current().is('_', '.', '-', 'E');
    }

    private boolean isNumberStart() {
        return reader.current().isDigit()
                || reader.current().is('-') && reader.next(1).isDigit()
                || reader.current().is('-') && reader.next(1).is('.') && reader.next(2).isDigit()
                || reader.current().is('.') && reader.next(1).isDigit();
    }

    private void skipLine() {
        while(!reader.current().isEOF() && !reader.current().isNewLine()) reader.consume();
    }

    private void skipTo(String s) throws EOFException {
        while(!reader.current().isEOF()) {
            if(reader.matches(s, true)) return;
            reader.consume();
        }
        throw new EOFException("No end of expression found.");
    }

    public boolean isSyntaxSignificant(char c) {
        return syntaxSignificant.contains(c);
    }

}
