package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
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
    Set<Token.Type> allowedArguments = Sets.newHashSet(Token.Type.STRING, Token.Type.NUMBER, Token.Type.IDENTIFIER);

    public Parser(String data) {
        this.data = data;
    }

    public Parser addFunction(String name, FunctionBuilder<? extends Function<?>> functionBuilder) {
        functions.put(name, functionBuilder);
        return this;
    }

    public List<Function<?>> parse() throws ParseException {
        Tokenizer tokenizer = new Tokenizer(data);
        List<Function<?>> builtFunctions = new GlueList<>();
        List<Token> functionBuilder = new GlueList<>();
        Token token = null;
        while(tokenizer.hasNext()) {
            try {
                token = tokenizer.fetch();
                functionBuilder.add(token);

                if(token.getType().equals(Token.Type.STATEMENT_END)) {
                    Token identifier = functionBuilder.remove(0);
                    checkType(identifier, Token.Type.IDENTIFIER); // First token must be identifier

                    if(!functions.containsKey(identifier.getContent()))
                        throw new ParseException("No such function " + identifier.getContent() + ": " + identifier.getStart());

                    checkType(functionBuilder.remove(0), Token.Type.BODY_BEGIN); // Second is body begin


                    List<Token> args = getArgs(functionBuilder); // Extract arguments, consume the rest.

                    functionBuilder.remove(0); // Remove body end

                    checkType(functionBuilder.remove(0), Token.Type.STATEMENT_END);

                    List<String> arg = args.stream().map(Token::getContent).collect(Collectors.toList());

                    FunctionBuilder<?> builder = functions.get(identifier.getContent());
                    if(arg.size() != builder.getArguments().size())
                        throw new ParseException("Expected " + builder.getArguments().size() + " arguments, found " + arg.size() + ": " + identifier.getStart());

                    builtFunctions.add(functions.get(identifier.getContent()).build(arg));

                    functionBuilder.clear();
                }
            } catch(TokenizerException e) {
                throw new ParseException("Failed to tokenize input", e);
            }

        }
        if(token != null) checkType(token, Token.Type.STATEMENT_END);
        return builtFunctions;
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
