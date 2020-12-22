package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.api.structures.parser.lang.constants.NumericConstant;
import com.dfsek.terra.api.structures.parser.lang.constants.StringConstant;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.parser.lang.keywords.IfKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.WhileKeyword;
import com.dfsek.terra.api.structures.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.BooleanAndOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.BooleanNotOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.BooleanOrOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.ConcatenationOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.DivisionOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.MultiplicationOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.NumberAdditionOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.SubtractionOperation;
import com.dfsek.terra.api.structures.parser.lang.operations.statements.EqualsStatement;
import com.dfsek.terra.api.structures.parser.lang.operations.statements.GreaterOrEqualsThanStatement;
import com.dfsek.terra.api.structures.parser.lang.operations.statements.GreaterThanStatement;
import com.dfsek.terra.api.structures.parser.lang.operations.statements.LessThanOrEqualsStatement;
import com.dfsek.terra.api.structures.parser.lang.operations.statements.LessThanStatement;
import com.dfsek.terra.api.structures.parser.lang.operations.statements.NotEqualsStatement;
import com.dfsek.terra.api.structures.parser.lang.variables.Assignment;
import com.dfsek.terra.api.structures.parser.lang.variables.BooleanVariable;
import com.dfsek.terra.api.structures.parser.lang.variables.Getter;
import com.dfsek.terra.api.structures.parser.lang.variables.NumberVariable;
import com.dfsek.terra.api.structures.parser.lang.variables.StringVariable;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.api.structures.tokenizer.Token;
import com.dfsek.terra.api.structures.tokenizer.Tokenizer;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import com.dfsek.terra.api.util.GlueList;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
    private final String data;
    private final Map<String, FunctionBuilder<? extends Function<?>>> functions = new HashMap<>();
    private final Set<String> keywords = Sets.newHashSet("if", "return", "while");

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
            if(blockLevel < 0) throw new ParseException("Dangling closing brace: " + t.getPosition());
        }
        if(blockLevel != 0) throw new ParseException("Dangling opening brace");

        return parseBlock(tokens, new HashMap<>());
    }


    @SuppressWarnings("unchecked")
    private Keyword<?> parseKeyword(List<Token> tokens, Map<String, Variable<?>> variableMap) throws ParseException {

        Token identifier = tokens.remove(0);
        ParserUtil.checkType(identifier, Token.Type.KEYWORD);
        if(!keywords.contains(identifier.getContent()))
            throw new ParseException("No such keyword " + identifier.getContent() + ": " + identifier.getPosition());
        Keyword<?> k = null;
        if(identifier.getContent().equals("if") || identifier.getContent().equals("while")) {

            ParserUtil.checkType(tokens.remove(0), Token.Type.GROUP_BEGIN);

            Returnable<?> comparator = parseExpression(tokens, true, variableMap);
            ParserUtil.checkReturnType(comparator, Returnable.ReturnType.BOOLEAN);

            ParserUtil.checkType(tokens.remove(0), Token.Type.GROUP_END);

            ParserUtil.checkType(tokens.remove(0), Token.Type.BLOCK_BEGIN);

            if(identifier.getContent().equals("if"))
                k = new IfKeyword(parseBlock(tokens, variableMap), (Returnable<Boolean>) comparator, identifier.getPosition()); // If statement
            else
                k = new WhileKeyword(parseBlock(tokens, variableMap), (Returnable<Boolean>) comparator, identifier.getPosition()); // While loop

        }
        return k;
    }

    @SuppressWarnings("unchecked")
    private Returnable<?> parseExpression(List<Token> tokens, boolean full, Map<String, Variable<?>> variableMap) throws ParseException {
        Token first = tokens.get(0);

        if(first.getType().equals(Token.Type.GROUP_BEGIN)) return parseGroup(tokens, variableMap);

        ParserUtil.checkType(first, Token.Type.IDENTIFIER, Token.Type.BOOLEAN, Token.Type.STRING, Token.Type.NUMBER, Token.Type.BOOLEAN_NOT, Token.Type.GROUP_BEGIN);

        boolean not = false;
        if(first.getType().equals(Token.Type.BOOLEAN_NOT)) {
            not = true;
            tokens.remove(0);
        }

        Token id = tokens.get(0);
        Returnable<?> expression;
        if(id.isConstant()) {
            Token constantToken = tokens.remove(0);
            Position position = constantToken.getPosition();
            switch(constantToken.getType()) {
                case NUMBER:
                    String content = constantToken.getContent();
                    expression = new NumericConstant(content.contains(".") ? Double.parseDouble(content) : Integer.parseInt(content), position);
                    break;
                case STRING:
                    expression = new StringConstant(constantToken.getContent(), position);
                    break;
                case BOOLEAN:
                    expression = new BooleanConstant(Boolean.parseBoolean(constantToken.getContent()), position);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported constant token: " + constantToken.getType() + " at position: " + position);
            }
        } else {
            if(functions.containsKey(id.getContent())) expression = parseFunction(tokens, false, variableMap);
            else if(variableMap.containsKey(id.getContent())) {
                ParserUtil.checkType(tokens.remove(0), Token.Type.IDENTIFIER);
                expression = new Getter(variableMap.get(id.getContent()));
            } else throw new ParseException("Unexpected token: " + id.getContent() + " at " + id.getPosition());
        }


        if(not) {
            ParserUtil.checkReturnType(expression, Returnable.ReturnType.BOOLEAN);
            expression = new BooleanNotOperation((Returnable<Boolean>) expression, expression.getPosition());
        }
        if(full && tokens.get(0).isBinaryOperator()) {
            return parseBinaryOperation(expression, tokens, variableMap);
        }
        return expression;
    }

    private Returnable<?> parseGroup(List<Token> tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        ParserUtil.checkType(tokens.remove(0), Token.Type.GROUP_BEGIN);
        Returnable<?> expression = parseExpression(tokens, true, variableMap);
        ParserUtil.checkType(tokens.remove(0), Token.Type.GROUP_END);
        return expression;
    }


    private BinaryOperation<?, ?> parseBinaryOperation(Returnable<?> left, List<Token> tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        Token binaryOperator = tokens.remove(0);
        ParserUtil.checkBinaryOperator(binaryOperator);

        Returnable<?> right = parseExpression(tokens, false, variableMap);

        Token other = tokens.get(0);
        if(other.isBinaryOperator() && (other.getType().equals(Token.Type.MULTIPLICATION_OPERATOR) || other.getType().equals(Token.Type.DIVISION_OPERATOR))) {
            return assemble(left, parseBinaryOperation(right, tokens, variableMap), binaryOperator);
        } else if(other.isBinaryOperator()) {
            return parseBinaryOperation(assemble(left, right, binaryOperator), tokens, variableMap);
        }
        return assemble(left, right, binaryOperator);
    }

    @SuppressWarnings("unchecked")
    private BinaryOperation<?, ?> assemble(Returnable<?> left, Returnable<?> right, Token binaryOperator) throws ParseException {
        if(binaryOperator.isStrictNumericOperator()) ParserUtil.checkArithmeticOperation(left, right, binaryOperator);
        if(binaryOperator.isStrictBooleanOperator()) ParserUtil.checkBooleanOperation(left, right, binaryOperator);
        switch(binaryOperator.getType()) {
            case ADDITION_OPERATOR:
                if(left.returnType().equals(Returnable.ReturnType.NUMBER) && right.returnType().equals(Returnable.ReturnType.NUMBER)) {
                    return new NumberAdditionOperation((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
                }
                return new ConcatenationOperation((Returnable<Object>) left, (Returnable<Object>) right, binaryOperator.getPosition());
            case SUBTRACTION_OPERATOR:
                return new SubtractionOperation((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case MULTIPLICATION_OPERATOR:
                return new MultiplicationOperation((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case DIVISION_OPERATOR:
                return new DivisionOperation((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case EQUALS_OPERATOR:
                return new EqualsStatement((Returnable<Object>) left, (Returnable<Object>) right, binaryOperator.getPosition());
            case NOT_EQUALS_OPERATOR:
                return new NotEqualsStatement((Returnable<Object>) left, (Returnable<Object>) right, binaryOperator.getPosition());
            case GREATER_THAN_OPERATOR:
                return new GreaterThanStatement((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case LESS_THAN_OPERATOR:
                return new LessThanStatement((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case GREATER_THAN_OR_EQUALS_OPERATOR:
                return new GreaterOrEqualsThanStatement((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case LESS_THAN_OR_EQUALS_OPERATOR:
                return new LessThanOrEqualsStatement((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case BOOLEAN_AND:
                return new BooleanAndOperation((Returnable<Boolean>) left, (Returnable<Boolean>) right, binaryOperator.getPosition());
            case BOOLEAN_OR:
                return new BooleanOrOperation((Returnable<Boolean>) left, (Returnable<Boolean>) right, binaryOperator.getPosition());
            default:
                throw new UnsupportedOperationException("Unsupported binary operator: " + binaryOperator.getType());
        }
    }

    private Variable<?> parseVariableDeclaration(List<Token> tokens, Returnable.ReturnType type) throws ParseException {
        ParserUtil.checkVarType(tokens.get(0), type); // Check for type mismatch
        switch(type) {
            case NUMBER:
                return new NumberVariable(0d, tokens.get(0).getPosition());
            case STRING:
                return new StringVariable("", tokens.get(0).getPosition());
            case BOOLEAN:
                return new BooleanVariable(false, tokens.get(0).getPosition());
        }
        throw new UnsupportedOperationException("Unsupported variable type: " + type);
    }

    @SuppressWarnings("unchecked")
    private Block parseBlock(List<Token> tokens, Map<String, Variable<?>> superVars) throws ParseException {
        List<Item<?>> parsedItems = new GlueList<>();

        Map<String, Variable<?>> parsedVariables = new HashMap<>(superVars); // New hashmap as to not mutate parent scope's declarations.

        Token first = tokens.get(0);

        ParserUtil.checkType(tokens.get(0), Token.Type.IDENTIFIER, Token.Type.KEYWORD, Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE);
        main:
        while(tokens.size() > 0) {
            Token token = tokens.get(0);
            ParserUtil.checkType(token, Token.Type.IDENTIFIER, Token.Type.KEYWORD, Token.Type.BLOCK_END, Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE);
            switch(token.getType()) {
                case KEYWORD:
                    parsedItems.add(parseKeyword(tokens, parsedVariables));
                    if(tokens.isEmpty()) break;
                    ParserUtil.checkType(tokens.get(0), Token.Type.IDENTIFIER, Token.Type.KEYWORD, Token.Type.BLOCK_END);
                    break;
                case IDENTIFIER:
                    if(parsedVariables.containsKey(token.getContent())) {
                        Variable<?> variable = parsedVariables.get(token.getContent());

                        parsedItems.add(parseAssignment(variable, tokens, parsedVariables));
                    } else parsedItems.add(parseFunction(tokens, true, parsedVariables));
                    if(tokens.isEmpty()) break;
                    ParserUtil.checkType(tokens.remove(0), Token.Type.STATEMENT_END, Token.Type.BLOCK_END);
                    break;
                case NUMBER_VARIABLE:
                case BOOLEAN_VARIABLE:
                case STRING_VARIABLE:
                    Variable<?> temp;
                    if(token.getType().equals(Token.Type.NUMBER_VARIABLE))
                        temp = parseVariableDeclaration(tokens, Returnable.ReturnType.NUMBER);
                    else if(token.getType().equals(Token.Type.STRING_VARIABLE))
                        temp = parseVariableDeclaration(tokens, Returnable.ReturnType.STRING);
                    else temp = parseVariableDeclaration(tokens, Returnable.ReturnType.BOOLEAN);
                    Token name = tokens.get(1);

                    ParserUtil.checkType(name, Token.Type.IDENTIFIER); // Name must be an identifier.

                    if(functions.containsKey(name.getContent()) || parsedVariables.containsKey(name.getContent()))
                        throw new ParseException(name.getContent() + " is already defined in this scope: " + name.getPosition());

                    parsedVariables.put(name.getContent(), temp);

                    ParserUtil.checkType(tokens.remove(0), Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.NUMBER_VARIABLE);

                    parsedItems.add(parseAssignment(temp, tokens, parsedVariables));
                    ParserUtil.checkType(tokens.remove(0), Token.Type.STATEMENT_END);
                    break;
                case BLOCK_END:
                    tokens.remove(0); // Remove block end.
                    break main;
            }
        }
        return new Block(parsedItems, first.getPosition());
    }

    @SuppressWarnings("unchecked")
    private Assignment<?> parseAssignment(Variable<?> variable, List<Token> tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        Token name = tokens.get(0);

        ParserUtil.checkType(tokens.remove(0), Token.Type.IDENTIFIER);

        ParserUtil.checkType(tokens.remove(0), Token.Type.ASSIGNMENT);

        Returnable<?> expression = parseExpression(tokens, true, variableMap);

        ParserUtil.checkReturnType(expression, variable.getType());

        return new Assignment<>((Variable<Object>) variable, (Returnable<Object>) expression, name.getPosition());
    }

    private Function<?> parseFunction(List<Token> tokens, boolean fullStatement, Map<String, Variable<?>> variableMap) throws ParseException {
        Token identifier = tokens.remove(0);
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER); // First token must be identifier

        if(!functions.containsKey(identifier.getContent()))
            throw new ParseException("No such function " + identifier.getContent() + ": " + identifier.getPosition());

        ParserUtil.checkType(tokens.remove(0), Token.Type.GROUP_BEGIN); // Second is body begin


        List<Returnable<?>> args = getArgs(tokens, variableMap); // Extract arguments, consume the rest.

        tokens.remove(0); // Remove body end

        if(fullStatement) ParserUtil.checkType(tokens.get(0), Token.Type.STATEMENT_END);

        FunctionBuilder<?> builder = functions.get(identifier.getContent());

        if(builder.argNumber() != -1 && args.size() != builder.argNumber())
            throw new ParseException("Expected " + builder.argNumber() + " arguments, found " + args.size() + ": " + identifier.getPosition());

        for(int i = 0; i < args.size(); i++) {
            Returnable<?> argument = args.get(i);
            if(builder.getArgument(i) == null)
                throw new ParseException("Unexpected argument at position " + i + " in function " + identifier.getContent() + ": " + identifier.getPosition());
            ParserUtil.checkReturnType(argument, builder.getArgument(i));
        }
        return builder.build(args, identifier.getPosition());
    }


    private List<Returnable<?>> getArgs(List<Token> tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        List<Returnable<?>> args = new GlueList<>();

        while(!tokens.get(0).getType().equals(Token.Type.GROUP_END)) {
            args.add(parseExpression(tokens, true, variableMap));
            ParserUtil.checkType(tokens.get(0), Token.Type.SEPARATOR, Token.Type.GROUP_END);
            if(tokens.get(0).getType().equals(Token.Type.SEPARATOR)) tokens.remove(0);
        }
        return args;
    }
}
