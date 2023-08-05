/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.lexer;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.dfsek.terra.addons.terrascript.exception.lexer.EOFException;
import com.dfsek.terra.addons.terrascript.exception.lexer.FormatException;
import com.dfsek.terra.addons.terrascript.exception.lexer.TokenizerException;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.lexer.Token.TokenType;


public class Lexer {
    public static final Set<Character> syntaxSignificant = Sets.newHashSet(':', ';', '(', ')', '"', ',', '\\', '=', '{', '}', '+', '-', '*',
                                                                           '/',
                                                                           '>', '<', '!'); // Reserved chars
    private final LookaheadStream reader;
    private Token current;
    
    public Lexer(String data) {
        reader = new LookaheadStream(data + '\0');
        current = tokenize();
    }
    
    public List<Token> analyze() {
        List<Token> tokens = new ArrayList<>();
        while(hasNext()) {
            tokens.add(consumeUnchecked());
        }
        tokens.add(current()); // Add EOF token
        return tokens;
    }
    
    /**
     * Get the first token.
     *
     * @return First token
     *
     * @throws ParseException If token does not exist
     */
    public Token current() {
        return current;
    }
    
    /**
     * Consume (get and remove) the first token.
     *
     * @return First token
     *
     * @throws ParseException If token does not exist
     */
    public Token consume(String wrongTypeMessage, TokenType expected, TokenType... more) {
        if(!current.isType(expected) && Arrays.stream(more).noneMatch(t -> t == current.type())) throw new ParseException(wrongTypeMessage,
                                                                                                                          current.position());
        return consumeUnchecked();
    }
    
    public Token consumeUnchecked() {
        if(current.type() == TokenType.END_OF_FILE) return current;
        Token temp = current;
        current = tokenize();
        return temp;
    }
    
    /**
     * Whether this {@code Tokenizer} contains additional tokens.
     *
     * @return {@code true} if more tokens are present, otherwise {@code false}
     */
    public boolean hasNext() {
        return current.type() != TokenType.END_OF_FILE;
    }
    
    private Token tokenize() throws TokenizerException {
        consumeWhitespace();
        SourcePosition position = reader.getPosition();
        
        // Skip line if comment
        while(reader.matchesString("//", true)) skipLine();
        
        // Skip multi line comment
        if(reader.matchesString("/*", true)) skipTo("*/");
        
        // Reached end of file
        if(reader.current().isEOF()) return new Token(reader.consume().toString(), TokenType.END_OF_FILE, position);
        
        // Check if operator token
        if(reader.matchesString("==", true))
            return new Token("==", TokenType.EQUALS_EQUALS, position);
        if(reader.matchesString("!=", true))
            return new Token("!=", TokenType.BANG_EQUALS, position);
        if(reader.matchesString(">=", true))
            return new Token(">=", TokenType.GREATER_EQUAL, position);
        if(reader.matchesString("<=", true))
            return new Token("<=", TokenType.LESS_EQUALS, position);
        if(reader.matchesString(">", true))
            return new Token(">", TokenType.GREATER, position);
        if(reader.matchesString("<", true))
            return new Token("<", TokenType.LESS, position);
        
        // Check if logical operator
        if(reader.matchesString("||", true))
            return new Token("||", TokenType.BOOLEAN_OR, position);
        if(reader.matchesString("&&", true))
            return new Token("&&", TokenType.BOOLEAN_AND, position);
        
        // Check if number
        if(isNumberStart()) {
            StringBuilder num = new StringBuilder();
            while(!reader.current().isEOF() && isNumberLike()) {
                num.append(reader.consume().getCharacter());
            }
            return new Token(num.toString(), TokenType.NUMBER, position);
        }
        
        // Check if string literal
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
                    throw new FormatException("No end of string literal found. ", position);
                string.append(reader.consume());
            }
            reader.consume(); // Consume last quote
            
            return new Token(string.toString(), TokenType.STRING, position);
        }
        
        if(reader.current().is('('))
            return new Token(reader.consume().toString(), TokenType.OPEN_PAREN, position);
        if(reader.current().is(')'))
            return new Token(reader.consume().toString(), TokenType.CLOSE_PAREN, position);
        if(reader.current().is(':'))
            return new Token(reader.consume().toString(), TokenType.COLON, position);
        if(reader.current().is(';'))
            return new Token(reader.consume().toString(), TokenType.STATEMENT_END, position);
        if(reader.current().is(','))
            return new Token(reader.consume().toString(), TokenType.SEPARATOR, position);
        
        if(reader.current().is('{')) return new Token(reader.consume().toString(), TokenType.BLOCK_BEGIN, position);
        if(reader.current().is('}')) return new Token(reader.consume().toString(), TokenType.BLOCK_END, position);
        
        if(reader.current().is('='))
            return new Token(reader.consume().toString(), TokenType.ASSIGNMENT, position);
        if(reader.current().is('+'))
            return new Token(reader.consume().toString(), TokenType.PLUS, position);
        if(reader.current().is('-'))
            return new Token(reader.consume().toString(), TokenType.MINUS,
                             position);
        if(reader.current().is('*'))
            return new Token(reader.consume().toString(), TokenType.STAR,
                             position);
        if(reader.current().is('/'))
            return new Token(reader.consume().toString(), TokenType.FORWARD_SLASH, position);
        if(reader.current().is('%'))
            return new Token(reader.consume().toString(), TokenType.MODULO_OPERATOR, position);
        if(reader.current().is('!'))
            return new Token(reader.consume().toString(), TokenType.BANG, position);
        
        // Read word
        StringBuilder token = new StringBuilder();
        while(!reader.current().isEOF() && !isSyntaxSignificant(reader.current().getCharacter())) {
            Char c = reader.consume();
            if(c.isWhitespace()) break;
            token.append(c.getCharacter());
        }
        String tokenString = token.toString();
        
        // Check if word is a keyword
        if(tokenString.equals("true"))
            return new Token(tokenString, TokenType.BOOLEAN, position);
        if(tokenString.equals("false"))
            return new Token(tokenString, TokenType.BOOLEAN, position);
        
        if(tokenString.equals("num"))
            return new Token(tokenString, TokenType.TYPE_NUMBER, position);
        if(tokenString.equals("str"))
            return new Token(tokenString, TokenType.TYPE_STRING, position);
        if(tokenString.equals("bool"))
            return new Token(tokenString, TokenType.TYPE_BOOLEAN, position);
        if(tokenString.equals("void"))
            return new Token(tokenString, TokenType.TYPE_VOID, position);
        
        if(tokenString.equals("fun"))
            return new Token(tokenString, TokenType.FUNCTION, position);
        
        if(tokenString.equals("if"))
            return new Token(tokenString, TokenType.IF_STATEMENT, position);
        if(tokenString.equals("else"))
            return new Token(tokenString, TokenType.ELSE, position);
        if(tokenString.equals("while"))
            return new Token(tokenString, TokenType.WHILE_LOOP, position);
        if(tokenString.equals("for"))
            return new Token(tokenString, TokenType.FOR_LOOP, position);
        
        if(tokenString.equals("return"))
            return new Token(tokenString, TokenType.RETURN, position);
        if(tokenString.equals("continue"))
            return new Token(tokenString, TokenType.CONTINUE, position);
        if(tokenString.equals("break"))
            return new Token(tokenString, TokenType.BREAK, position);
        if(tokenString.equals("fail"))
            return new Token(tokenString, TokenType.FAIL, position);
        
        // If not keyword, assume it is an identifier
        return new Token(tokenString, TokenType.IDENTIFIER, position);
    }
    
    private void skipLine() {
        while(!reader.current().isEOF() && !reader.current().isNewLine()) reader.consume();
        consumeWhitespace();
    }
    
    private void consumeWhitespace() {
        while(!reader.current().isEOF() && reader.current().isWhitespace()) reader.consume(); // Consume whitespace.
    }
    
    private void skipTo(String s) throws EOFException {
        SourcePosition begin = reader.getPosition();
        while(!reader.current().isEOF()) {
            if(reader.matchesString(s, true)) {
                consumeWhitespace();
                return;
            }
            reader.consume();
        }
        throw new EOFException("No end of expression found.", begin);
    }
    
    private boolean isNumberLike() {
        return reader.current().isDigit()
               || reader.current().is('_', '.', 'E');
    }
    
    private boolean isNumberStart() {
        return reader.current().isDigit()
               || reader.current().is('.') && reader.peek().isDigit();
    }
    
    public boolean isSyntaxSignificant(char c) {
        return syntaxSignificant.contains(c);
    }
}
