package com.dfsek.terra.api.structures.tokenizer;

import com.dfsek.terra.api.structures.tokenizer.exceptions.EOFException;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import com.dfsek.terra.api.structures.tokenizer.group.Group;
import com.dfsek.terra.api.util.GlueList;
import com.google.common.collect.Sets;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

public class Tokenizer {
    private final Lookahead reader;

    private final Set<Character> syntaxSignificant = Sets.newHashSet(';', '(', ')', '"', '[', ']', ',');


    public Tokenizer(String data) {
        reader = new Lookahead(new StringReader(data + '\0'));
    }

    public List<TokenizedStatement> tokenize() {
        List<TokenizedStatement> tokens = new GlueList<>();
        while(reader.current().isEOF()) {
            Char c = reader.current();
        }

        return tokens;
    }

    public Token fetch() throws TokenizerException {

        while(!reader.current().isEOF() && reader.current().isWhitespace()) reader.consume();
        if(reader.current().isEOF()) return null; // EOF

        if(reader.matches("//", true)) skipLine(); // Skip line if comment

        if(reader.matches("/*", true)) skipTo("*/");

        if(isNumberStart()) {
            StringBuilder num = new StringBuilder();
            while(!reader.current().isEOF() && isNumberLike()) {
                num.append(reader.consume());
            }
            return new Token(num.toString(), Token.Type.NUMBER);
        }

        if(reader.current().is('"')) {
            reader.consume(); // Consume first quote
            StringBuilder string = new StringBuilder();
            while(!reader.current().isEOF() && !reader.current().is('"')) {
                string.append(reader.consume());
            }
            reader.consume(); // Consume last quote
            return new Token(string.toString(), Token.Type.STRING);
        }

        if(reader.current().is('(')) return new Token(reader.consume().toString(), Token.Type.BODY_BEGIN);
        if(reader.current().is(')')) return new Token(reader.consume().toString(), Token.Type.BODY_END);
        if(reader.current().is(';')) return new Token(reader.consume().toString(), Token.Type.STATEMENT_END);
        if(reader.current().is(',')) return new Token(reader.consume().toString(), Token.Type.SEPARATOR);

        StringBuilder token = new StringBuilder();
        while(!reader.current().isEOF() && !isSyntaxSignificant(reader.current().getCharacter())) {
            Char c = reader.consume();
            if(!c.isWhitespace()) token.append(c);
        }

        return new Token(token.toString(), Token.Type.IDENTIFIER);
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

    /**
     * Read to the end of a group, consuming all
     *
     * @param g
     * @return
     */
    private String readToEndOfGroup(Group g) {
        StringBuilder builder = new StringBuilder();
        do {
            Char current = reader.consume();

        } while(reader.current().getCharacter() != g.getEnd());
        return builder.toString();
    }

    public boolean isSyntaxSignificant(char c) {
        return syntaxSignificant.contains(c);
    }

}
