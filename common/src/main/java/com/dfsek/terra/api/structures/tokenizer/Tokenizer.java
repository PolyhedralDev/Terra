package com.dfsek.terra.api.structures.tokenizer;

import com.dfsek.terra.api.structures.tokenizer.exceptions.EOFException;
import com.dfsek.terra.api.structures.tokenizer.exceptions.FormatException;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import com.google.common.collect.Sets;

import java.io.StringReader;
import java.util.Set;

public class Tokenizer {
    private final Lookahead reader;

    public static final Set<Character> syntaxSignificant = Sets.newHashSet(';', '(', ')', '"', ',', '\\', '=', '{', '}', '+', '-', '*', '/', '>', '<', '!'); // Reserved chars


    public Tokenizer(String data) {
        reader = new Lookahead(new StringReader(data + '\0'));
    }

    public boolean hasNext() {

        int whiteEnd = 0;

        while(!reader.next(whiteEnd).isEOF() && reader.next(whiteEnd).isWhitespace()) whiteEnd++; // Consume whitespace.

        return !reader.next(whiteEnd).isEOF();
    }

    public Token fetch() throws TokenizerException {
        while(!reader.current().isEOF() && reader.current().isWhitespace()) reader.consume();
        if(reader.current().isEOF()) return null; // EOF

        if(reader.matches("//", true)) skipLine(); // Skip line if comment

        if(reader.matches("/*", true)) skipTo("*/"); // Skip multi line comment


        if(reader.matches("==", true))
            return new Token("==", Token.Type.EQUALS_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("!=", true))
            return new Token("!=", Token.Type.NOT_EQUALS_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches(">=", true))
            return new Token(">=", Token.Type.GREATER_THAN_OR_EQUALS_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("<=", true))
            return new Token("<=", Token.Type.LESS_THAN_OR_EQUALS_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches(">", true))
            return new Token(">", Token.Type.GREATER_THAN_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("<", true))
            return new Token("<", Token.Type.LESS_THAN_OPERATOR, new Position(reader.getLine(), reader.getIndex()));


        if(reader.matches("||", true))
            return new Token("||", Token.Type.BOOLEAN_OR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.matches("&&", true))
            return new Token("&&", Token.Type.BOOLEAN_AND, new Position(reader.getLine(), reader.getIndex()));


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
            return new Token(reader.consume().toString(), Token.Type.GROUP_BEGIN, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is(')'))
            return new Token(reader.consume().toString(), Token.Type.GROUP_END, new Position(reader.getLine(), reader.getIndex()));
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
        if(reader.current().is('-'))
            return new Token(reader.consume().toString(), Token.Type.SUBTRACTION_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('*'))
            return new Token(reader.consume().toString(), Token.Type.MULTIPLICATION_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('/'))
            return new Token(reader.consume().toString(), Token.Type.DIVISION_OPERATOR, new Position(reader.getLine(), reader.getIndex()));
        if(reader.current().is('!'))
            return new Token(reader.consume().toString(), Token.Type.BOOLEAN_NOT, new Position(reader.getLine(), reader.getIndex()));

        StringBuilder token = new StringBuilder();
        while(!reader.current().isEOF() && !isSyntaxSignificant(reader.current().getCharacter())) {
            Char c = reader.consume();
            if(c.isWhitespace()) break;
            token.append(c);
        }

        String tokenString = token.toString();

        if(tokenString.equals("true"))
            return new Token(tokenString, Token.Type.BOOLEAN, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("false"))
            return new Token(tokenString, Token.Type.BOOLEAN, new Position(reader.getLine(), reader.getIndex()));

        if(tokenString.equals("num"))
            return new Token(tokenString, Token.Type.NUMBER_VARIABLE, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("str"))
            return new Token(tokenString, Token.Type.STRING_VARIABLE, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("bool"))
            return new Token(tokenString, Token.Type.BOOLEAN_VARIABLE, new Position(reader.getLine(), reader.getIndex()));

        if(tokenString.equals("if"))
            return new Token(tokenString, Token.Type.IF_STATEMENT, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("while"))
            return new Token(tokenString, Token.Type.WHILE_LOOP, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("for"))
            return new Token(tokenString, Token.Type.FOR_LOOP, new Position(reader.getLine(), reader.getIndex()));

        if(tokenString.equals("return"))
            return new Token(tokenString, Token.Type.RETURN, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("continue"))
            return new Token(tokenString, Token.Type.CONTINUE, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("break"))
            return new Token(tokenString, Token.Type.BREAK, new Position(reader.getLine(), reader.getIndex()));
        if(tokenString.equals("fail"))
            return new Token(tokenString, Token.Type.FAIL, new Position(reader.getLine(), reader.getIndex()));

        if(tokenString.equals("id"))
            return new Token(tokenString, Token.Type.ID, new Position(reader.getLine(), reader.getIndex()));

        return new Token(tokenString, Token.Type.IDENTIFIER, new Position(reader.getLine(), reader.getIndex()));
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
        consumeWhitespace();
    }

    private void consumeWhitespace() {
        while(!reader.current().isEOF() && reader.current().isWhitespace()) reader.consume(); // Consume whitespace.
    }

    private void skipTo(String s) throws EOFException {
        while(!reader.current().isEOF()) {
            if(reader.matches(s, true)) {
                consumeWhitespace();
                return;
            }
            reader.consume();
        }
        throw new EOFException("No end of expression found.");
    }

    public boolean isSyntaxSignificant(char c) {
        return syntaxSignificant.contains(c);
    }

}
