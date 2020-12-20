package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.Expression;
import com.dfsek.terra.api.structures.parser.lang.Function;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Statement;
import com.dfsek.terra.api.structures.parser.lang.keywords.IfKeyword;
import com.dfsek.terra.api.structures.parser.lang.statements.EqualsStatement;
import com.dfsek.terra.api.structures.tokenizer.Token;
import com.dfsek.terra.api.structures.tokenizer.Tokenizer;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import com.dfsek.terra.api.util.GlueList;
import com.google.common.collect.Sets;

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


    private Keyword<?> parseKeyword(List<Token> tokens, List<Token> functionAndArguments) throws ParseException {

        Token identifier = functionAndArguments.remove(0);
        checkType(identifier, Token.Type.IDENTIFIER);
        if(!keywords.contains(identifier.getContent()))
            throw new ParseException("No such keyword " + identifier.getContent() + ": " + identifier.getStart());
        Keyword<?> k = null;
        if(identifier.getContent().equals("if")) {

            checkType(functionAndArguments.remove(0), Token.Type.BODY_BEGIN);

            Expression<?> left = parseExpression(functionAndArguments);

            Statement statement = null;
            Token comparator = functionAndArguments.remove(0);
            checkType(comparator, Token.Type.BOOLEAN_OPERATOR);

            Expression<?> right = parseExpression(functionAndArguments);

            checkType(functionAndArguments.remove(0), Token.Type.BODY_END);
            if(comparator.getContent().equals("==")) {
                statement = new EqualsStatement(left, right);
            }

            k = new IfKeyword(parseBlock(tokens), statement);

        }
        return k;
    }

    private Expression<?> parseExpression(List<Token> tokens) throws ParseException {
        if(tokens.get(0).isConstant()) {
            return new ConstantExpression(tokens.remove(0).getContent());
        } else return parseFunction(tokens, false);
    }

    private Block parseBlock(List<Token> tokens) throws ParseException {
        List<Item<?>> parsedItems = new GlueList<>();
        List<Token> functionArgs = new GlueList<>();

        while(tokens.size() > 0) {
            Token token = tokens.remove(0);
            if(token.getType().equals(Token.Type.BLOCK_END)) break;
            functionArgs.add(token);
            if(token.getType().equals(Token.Type.STATEMENT_END)) {
                parsedItems.add(parseFunction(functionArgs, true));
                functionArgs.clear();
            } else if(token.getType().equals(Token.Type.BLOCK_BEGIN)) {
                parsedItems.add(parseKeyword(tokens, functionArgs));
                functionArgs.clear();
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

        if(fullStatement) checkType(functionAndArguments.remove(0), Token.Type.STATEMENT_END);

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

    private void checkType(Token token, Token.Type expected) throws ParseException {
        if(!token.getType().equals(expected))
            throw new ParseException("Expected " + expected + " but found " + token.getType() + ": " + token.getStart());
    }
}
