/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.tokenizer;

import com.google.common.collect.Sets;

import java.io.StringReader;
import java.util.Set;
import java.util.Stack;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.tokenizer.exceptions.EOFException;
import com.dfsek.terra.addons.terrascript.tokenizer.exceptions.FormatException;
import com.dfsek.terra.addons.terrascript.tokenizer.exceptions.TokenizerException;


public class Tokenizer {
    public static final Set<Character> syntaxSignificant = Sets.newHashSet(';', '(', ')', '"', ',', '\\', '=', '{', '}', '+', '-', '*', '/',
                                                                           '>', '<', '!'); // Reserved chars
    private final LookaheadStream reader;
    private final Stack<Token> bracketStack = new Stack<>();
    private Token current;
    
    public Tokenizer(String data) {
        reader = new LookaheadStream(new StringReader(data + '\0'));
        current = tokenize();
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
    public Token consume() {
        if (current.getType() == Token.Type.END_OF_FILE) return current;
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
        return current.getType() != Token.Type.END_OF_FILE;
    }
    
    private Token tokenize() throws TokenizerException {
        consumeWhitespace();
        
        // Skip line if comment
        while(reader.matchesString("//", true)) skipLine();
        
        // Skip multi line comment
        if(reader.matchesString("/*", true)) skipTo("*/");
        
        // Reached end of file
        if(reader.current().isEOF()) {
            if (!bracketStack.isEmpty()) throw new ParseException("Dangling closing brace", bracketStack.peek().getPosition());
            return new Token(reader.consume().toString(), Token.Type.END_OF_FILE, reader.getPosition());
        }
        
        // Check if operator token
        if(reader.matchesString("==", true))
            return new Token("==", Token.Type.EQUALS_OPERATOR, reader.getPosition());
        if(reader.matchesString("!=", true))
            return new Token("!=", Token.Type.NOT_EQUALS_OPERATOR, reader.getPosition());
        if(reader.matchesString(">=", true))
            return new Token(">=", Token.Type.GREATER_THAN_OR_EQUALS_OPERATOR, reader.getPosition());
        if(reader.matchesString("<=", true))
            return new Token("<=", Token.Type.LESS_THAN_OR_EQUALS_OPERATOR, reader.getPosition());
        if(reader.matchesString(">", true))
            return new Token(">", Token.Type.GREATER_THAN_OPERATOR, reader.getPosition());
        if(reader.matchesString("<", true))
            return new Token("<", Token.Type.LESS_THAN_OPERATOR, reader.getPosition());
        
        // Check if logical operator
        if(reader.matchesString("||", true))
            return new Token("||", Token.Type.BOOLEAN_OR, reader.getPosition());
        if(reader.matchesString("&&", true))
            return new Token("&&", Token.Type.BOOLEAN_AND, reader.getPosition());
        
        // Check if number
        if(isNumberStart()) {
            StringBuilder num = new StringBuilder();
            while(!reader.current().isEOF() && isNumberLike()) {
                num.append(reader.consume());
            }
            return new Token(num.toString(), Token.Type.NUMBER, reader.getPosition());
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
                    throw new FormatException("No end of string literal found. ", reader.getPosition());
                string.append(reader.consume());
            }
            reader.consume(); // Consume last quote
            
            return new Token(string.toString(), Token.Type.STRING, reader.getPosition());
        }
        
        if(reader.current().is('('))
            return new Token(reader.consume().toString(), Token.Type.GROUP_BEGIN, reader.getPosition());
        if(reader.current().is(')'))
            return new Token(reader.consume().toString(), Token.Type.GROUP_END, reader.getPosition());
        if(reader.current().is(';'))
            return new Token(reader.consume().toString(), Token.Type.STATEMENT_END, reader.getPosition());
        if(reader.current().is(','))
            return new Token(reader.consume().toString(), Token.Type.SEPARATOR, reader.getPosition());
        
        if(reader.current().is('{')) {
            Token token = new Token(reader.consume().toString(), Token.Type.BLOCK_BEGIN, reader.getPosition());
            bracketStack.push(token);
            return token;
        }
        if(reader.current().is('}')) {
            if(bracketStack.isEmpty()) throw new ParseException("Dangling opening brace", new SourcePosition(0, 0));
            bracketStack.pop();
            return new Token(reader.consume().toString(), Token.Type.BLOCK_END, reader.getPosition());
        }
        
        if(reader.current().is('='))
            return new Token(reader.consume().toString(), Token.Type.ASSIGNMENT, reader.getPosition());
        if(reader.current().is('+'))
            return new Token(reader.consume().toString(), Token.Type.ADDITION_OPERATOR, reader.getPosition());
        if(reader.current().is('-'))
            return new Token(reader.consume().toString(), Token.Type.SUBTRACTION_OPERATOR,
                             reader.getPosition());
        if(reader.current().is('*'))
            return new Token(reader.consume().toString(), Token.Type.MULTIPLICATION_OPERATOR,
                             reader.getPosition());
        if(reader.current().is('/'))
            return new Token(reader.consume().toString(), Token.Type.DIVISION_OPERATOR, reader.getPosition());
        if(reader.current().is('%'))
            return new Token(reader.consume().toString(), Token.Type.MODULO_OPERATOR, reader.getPosition());
        if(reader.current().is('!'))
            return new Token(reader.consume().toString(), Token.Type.BOOLEAN_NOT, reader.getPosition());
        
        // Read word
        StringBuilder token = new StringBuilder();
        while(!reader.current().isEOF() && !isSyntaxSignificant(reader.current().getCharacter())) {
            Char c = reader.consume();
            if(c.isWhitespace()) break;
            token.append(c);
        }
        String tokenString = token.toString();
        
        // Check if word is a keyword
        if(tokenString.equals("true"))
            return new Token(tokenString, Token.Type.BOOLEAN, reader.getPosition());
        if(tokenString.equals("false"))
            return new Token(tokenString, Token.Type.BOOLEAN, reader.getPosition());
        
        if(tokenString.equals("num"))
            return new Token(tokenString, Token.Type.TYPE_NUMBER, reader.getPosition());
        if(tokenString.equals("str"))
            return new Token(tokenString, Token.Type.TYPE_STRING, reader.getPosition());
        if(tokenString.equals("bool"))
            return new Token(tokenString, Token.Type.TYPE_BOOLEAN, reader.getPosition());
        if(tokenString.equals("void"))
            return new Token(tokenString, Token.Type.TYPE_VOID, reader.getPosition());
        
        if(tokenString.equals("if"))
            return new Token(tokenString, Token.Type.IF_STATEMENT, reader.getPosition());
        if(tokenString.equals("else"))
            return new Token(tokenString, Token.Type.ELSE, reader.getPosition());
        if(tokenString.equals("while"))
            return new Token(tokenString, Token.Type.WHILE_LOOP, reader.getPosition());
        if(tokenString.equals("for"))
            return new Token(tokenString, Token.Type.FOR_LOOP, reader.getPosition());
        
        if(tokenString.equals("return"))
            return new Token(tokenString, Token.Type.RETURN, reader.getPosition());
        if(tokenString.equals("continue"))
            return new Token(tokenString, Token.Type.CONTINUE, reader.getPosition());
        if(tokenString.equals("break"))
            return new Token(tokenString, Token.Type.BREAK, reader.getPosition());
        if(tokenString.equals("fail"))
            return new Token(tokenString, Token.Type.FAIL, reader.getPosition());
        
        // If not keyword, assume it is an identifier
        return new Token(tokenString, Token.Type.IDENTIFIER, reader.getPosition());
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
               || reader.current().is('.') && reader.next(1).isDigit();
    }
    
    public boolean isSyntaxSignificant(char c) {
        return syntaxSignificant.contains(c);
    }
}
