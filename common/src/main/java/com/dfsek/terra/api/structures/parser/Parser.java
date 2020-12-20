package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.Executable;
import com.dfsek.terra.api.structures.parser.lang.Function;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Statement;
import com.dfsek.terra.api.structures.parser.lang.keywords.IfKeyword;
import com.dfsek.terra.api.structures.parser.lang.statements.EqualsStatement;
import com.dfsek.terra.api.structures.parser.lang.statements.NotEqualsStatement;
import com.dfsek.terra.api.structures.tokenizer.Token;
import com.dfsek.terra.api.structures.tokenizer.Tokenizer;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import com.dfsek.terra.api.util.GlueList;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Parser {
    private final String data;
    private final Map<String, FunctionBuilder<? extends Function<?>>> functions = new HashMap<>();
    private final Set<String> keywords = Sets.newHashSet("if");

    Set<Token.Type> allowedArguments = Sets.newHashSet(Token.Type.STRING, Token.Type.NUMBER, Token.Type.IDENTIFIER);

    public Parser(String data) {
        this.data = data;
    }

    public Parser addFunction(String name, FunctionBuilder<? extends Function<?>> functionBuilder) {
        functions.put(name, functionBuilder);
        return this;
    }

    public Block parse() throws ParseException {
        Tokenizer tokenizer = new Tokenizer(data);

        List<Token> tokens = new GlueList<>();
        try {
            while(tokenizer.hasNext()) tokens.add(tokenizer.fetch());
        } catch(TokenizerException e) {
            throw new ParseException("Failed to tokenize input", e);
        }
        // Check for dangling brackets
        int blockLevel = 0;
        for(Token t : tokens) {
            if(t.getType().equals(Token.Type.BLOCK_BEGIN)) blockLevel++;
            else if(t.getType().equals(Token.Type.BLOCK_END)) blockLevel--;
            if(blockLevel < 0) throw new ParseException("Dangling closing brace: " + t.getStart());
        }
        if(blockLevel != 0) throw new ParseException("Dangling opening brace");

        return parseBlock(tokens);
    }


    private Keyword<?> parseKeyword(List<Token> tokens) throws ParseException {

        Token identifier = tokens.remove(0);
        checkType(identifier, Token.Type.KEYWORD);
        if(!keywords.contains(identifier.getContent()))
            throw new ParseException("No such keyword " + identifier.getContent() + ": " + identifier.getStart());
        Keyword<?> k = null;
        if(identifier.getContent().equals("if")) {

            checkType(tokens.remove(0), Token.Type.BODY_BEGIN);

            Executable<?> left = parseExpression(tokens);

            Statement statement = null;
            Token comparator = tokens.remove(0);
            checkType(comparator, Token.Type.BOOLEAN_OPERATOR);

            Executable<?> right = parseExpression(tokens);

            checkType(tokens.remove(0), Token.Type.BODY_END);
            if(comparator.getContent().equals("==")) {
                statement = new EqualsStatement(left, right);
            } else if(comparator.getContent().equals("!=")) {
                statement = new NotEqualsStatement(left, right);
            }

            checkType(tokens.remove(0), Token.Type.BLOCK_BEGIN);

            k = new IfKeyword(parseBlock(tokens), statement);

        }
        return k;
    }

    private Executable<?> parseExpression(List<Token> tokens) throws ParseException {
        if(tokens.get(0).isConstant()) {
            return new ConstantExpression(tokens.remove(0).getContent());
        } else return parseFunction(tokens, false);
    }

    private Block parseBlock(List<Token> tokens) throws ParseException {
        List<Item<?>> parsedItems = new GlueList<>();
        checkType(tokens.get(0), Token.Type.IDENTIFIER, Token.Type.KEYWORD);
        main:
        while(tokens.size() > 0) {
            Token token = tokens.get(0);
            checkType(token, Token.Type.IDENTIFIER, Token.Type.KEYWORD, Token.Type.BLOCK_END);
            switch(token.getType()) {
                case KEYWORD:
                    parsedItems.add(parseKeyword(tokens));
                    if(tokens.isEmpty()) break;
                    checkType(tokens.get(0), Token.Type.IDENTIFIER, Token.Type.KEYWORD, Token.Type.BLOCK_END);
                    break;
                case IDENTIFIER:
                    parsedItems.add(parseFunction(tokens, true));
                    if(tokens.isEmpty()) break;
                    checkType(tokens.remove(0), Token.Type.STATEMENT_END, Token.Type.BLOCK_END);
                    break;
                case BLOCK_END:
                    tokens.remove(0);
                    break main;
            }
        }
        return new Block(parsedItems);
    }

    private Function<?> parseFunction(List<Token> functionAndArguments, boolean fullStatement) throws ParseException {
        Token identifier = functionAndArguments.remove(0);
        checkType(identifier, Token.Type.IDENTIFIER); // First token must be identifier

        if(!functions.containsKey(identifier.getContent()))
            throw new ParseException("No such function " + identifier.getContent() + ": " + identifier.getStart());

        checkType(functionAndArguments.remove(0), Token.Type.BODY_BEGIN); // Second is body begin


        List<Token> args = getArgs(functionAndArguments); // Extract arguments, consume the rest.

        functionAndArguments.remove(0); // Remove body end

        if(fullStatement) checkType(functionAndArguments.get(0), Token.Type.STATEMENT_END);

        List<String> arg = args.stream().map(Token::getContent).collect(Collectors.toList());

        FunctionBuilder<?> builder = functions.get(identifier.getContent());
        if(arg.size() != builder.getArguments() && builder.getArguments() != -1)
            throw new ParseException("Expected " + builder.getArguments() + " arguments, found " + arg.size() + ": " + identifier.getStart());
        return functions.get(identifier.getContent()).build(arg);
    }

    private List<Token> getArgs(List<Token> functionBuilder) throws ParseException {
        List<Token> args = new GlueList<>();
        boolean expectingSeparator = false;

        while(!functionBuilder.get(0).getType().equals(Token.Type.BODY_END)) {
            Token current = functionBuilder.remove(0);
            if(expectingSeparator) {
                checkType(current, Token.Type.SEPARATOR);
                expectingSeparator = false;
            } else {
                if(!allowedArguments.contains(current.getType()))
                    throw new ParseException("Token type " + current.getType() + " not allowed in arguments: " + current.getStart());
                args.add(current);
                expectingSeparator = true;
            }
        }
        return args;
    }

    private void checkType(Token token, Token.Type... expected) throws ParseException {
        for(Token.Type type : expected) if(token.getType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(expected) + " but found " + token.getType() + ": " + token.getStart());
    }
}
