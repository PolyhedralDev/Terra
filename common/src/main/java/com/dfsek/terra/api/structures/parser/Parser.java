package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.api.structures.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.constants.NumericConstant;
import com.dfsek.terra.api.structures.parser.lang.constants.StringConstant;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.parser.lang.functions.builtin.AbsFunction;
import com.dfsek.terra.api.structures.parser.lang.functions.builtin.PowFunction;
import com.dfsek.terra.api.structures.parser.lang.functions.builtin.SqrtFunction;
import com.dfsek.terra.api.structures.parser.lang.keywords.BreakKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.ContinueKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.FailKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.ReturnKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.looplike.ForKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.looplike.IfKeyword;
import com.dfsek.terra.api.structures.parser.lang.keywords.looplike.WhileKeyword;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class Parser {
    private final String data;
    private final Map<String, FunctionBuilder<? extends Function<?>>> functions = new HashMap<>();
    private final Set<String> builtinFunctions = Sets.newHashSet("abs", "sqrt", "pow");

    private String id;

    public Parser(String data) {
        this.data = data;
    }

    public Parser addFunction(String name, FunctionBuilder<? extends Function<?>> functionBuilder) {
        functions.put(name, functionBuilder);
        return this;
    }

    public String getID() {
        return id;
    }

    /**
     * Parse input
     *
     * @return executable {@link Block}
     * @throws ParseException If parsing fails.
     */
    public Block parse() throws ParseException {
        Tokenizer tokenizer = new Tokenizer(data);

        TokenHolder tokens = new TokenHolder();
        try {
            Token t = tokenizer.fetch();
            while(t != null) {
                tokens.add(t);
                t = tokenizer.fetch();
            }
        } catch(TokenizerException e) {
            throw new ParseException("Failed to tokenize input", new Position(0, 0), e);
        }

        // Parse ID
        ParserUtil.checkType(tokens.consume(), Token.Type.ID); // First token must be ID
        Token idToken = tokens.get();
        ParserUtil.checkType(tokens.consume(), Token.Type.STRING); // Second token must be string literal containing ID
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        this.id = idToken.getContent();

        // Check for dangling brackets
        int blockLevel = 0;
        for(Token t : tokens.getTokens()) {
            if(t.getType().equals(Token.Type.BLOCK_BEGIN)) blockLevel++;
            else if(t.getType().equals(Token.Type.BLOCK_END)) blockLevel--;
            if(blockLevel < 0) throw new ParseException("Dangling closing brace", t.getPosition());
        }
        if(blockLevel != 0)
            throw new ParseException("Dangling opening brace", tokens.getTokens().get(tokens.getTokens().size() - 1).getPosition());

        return parseBlock(tokens, new HashMap<>());
    }


    private Keyword<?> parseLoopLike(TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {

        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP);

        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN);

        switch(identifier.getType()) {
            case FOR_LOOP:
                return parseForLoop(tokens, variableMap, identifier.getPosition());
            case IF_STATEMENT:
                return parseIfStatement(tokens, variableMap, identifier.getPosition());
            case WHILE_LOOP:
                return parseWhileLoop(tokens, variableMap, identifier.getPosition());
            default:
                throw new UnsupportedOperationException("Unknown keyword " + identifier.getContent() + ": " + identifier.getPosition());
        }
    }

    private WhileKeyword parseWhileLoop(TokenHolder tokens, Map<String, Variable<?>> variableMap, Position start) throws ParseException {
        Returnable<?> first = parseExpression(tokens, true, variableMap);
        ParserUtil.checkReturnType(first, Returnable.ReturnType.BOOLEAN);

        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);

        return new WhileKeyword(parseStatementBlock(tokens, variableMap), (Returnable<Boolean>) first, start); // While loop
    }

    private IfKeyword parseIfStatement(TokenHolder tokens, Map<String, Variable<?>> variableMap, Position start) throws ParseException {
        Returnable<?> condition = parseExpression(tokens, true, variableMap);
        ParserUtil.checkReturnType(condition, Returnable.ReturnType.BOOLEAN);

        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);

        Block elseBlock = null;
        Block statement = parseStatementBlock(tokens, variableMap);

        List<IfKeyword.Pair<Returnable<Boolean>, Block>> elseIf = new GlueList<>();

        while(tokens.hasNext() && tokens.get().getType().equals(Token.Type.ELSE)) {
            tokens.consume(); // Consume else.
            if(tokens.get().getType().equals(Token.Type.IF_STATEMENT)) {
                tokens.consume(); // Consume if.
                Returnable<?> elseCondition = parseExpression(tokens, true, variableMap);
                ParserUtil.checkReturnType(elseCondition, Returnable.ReturnType.BOOLEAN);
                elseIf.add(new IfKeyword.Pair<>((Returnable<Boolean>) elseCondition, parseStatementBlock(tokens, variableMap)));
            } else {
                elseBlock = parseStatementBlock(tokens, variableMap);
                break; // Else must be last.
            }
        }

        return new IfKeyword(statement, (Returnable<Boolean>) condition, elseIf, elseBlock, start); // If statement
    }

    private Block parseStatementBlock(TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {

        if(tokens.get().getType().equals(Token.Type.BLOCK_BEGIN)) {
            ParserUtil.checkType(tokens.consume(), Token.Type.BLOCK_BEGIN);
            Block block = parseBlock(tokens, variableMap);
            ParserUtil.checkType(tokens.consume(), Token.Type.BLOCK_END);
            return block;
        } else {
            Position position = tokens.get().getPosition();
            Block block = new Block(Collections.singletonList(parseItem(tokens, variableMap)), position);
            ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
            return block;
        }
    }

    private ForKeyword parseForLoop(TokenHolder tokens, Map<String, Variable<?>> variableMap, Position start) throws ParseException {
        Token f = tokens.get();
        ParserUtil.checkType(f, Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.IDENTIFIER);
        Item<?> initializer;
        if(f.isVariableDeclaration()) {
            Variable<?> forVar = parseVariableDeclaration(tokens, ParserUtil.getVariableReturnType(f));
            ParserUtil.checkType(tokens.consume(), Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.NUMBER_VARIABLE);
            Token name = tokens.get();

            if(functions.containsKey(name.getContent()) || variableMap.containsKey(name.getContent()) || builtinFunctions.contains(name.getContent()))
                throw new ParseException(name.getContent() + " is already defined in this scope", name.getPosition());

            initializer = parseAssignment(forVar, tokens, variableMap);
            variableMap.put(name.getContent(), forVar);
        } else initializer = parseExpression(tokens, true, variableMap);
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        Returnable<?> conditional = parseExpression(tokens, true, variableMap);
        ParserUtil.checkReturnType(conditional, Returnable.ReturnType.BOOLEAN);
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);

        Item<?> incrementer;
        Token token = tokens.get();
        if(variableMap.containsKey(token.getContent())) { // Assume variable assignment
            Variable<?> variable = variableMap.get(token.getContent());
            incrementer = parseAssignment(variable, tokens, variableMap);
        } else incrementer = parseFunction(tokens, true, variableMap);

        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);

        return new ForKeyword(parseStatementBlock(tokens, variableMap), initializer, (Returnable<Boolean>) conditional, incrementer, start);
    }

    private Returnable<?> parseExpression(TokenHolder tokens, boolean full, Map<String, Variable<?>> variableMap) throws ParseException {
        boolean booleanInverted = false; // Check for boolean not operator
        if(tokens.get().getType().equals(Token.Type.BOOLEAN_NOT)) {
            booleanInverted = true;
            tokens.consume();
        }

        Token id = tokens.get();

        ParserUtil.checkType(id, Token.Type.IDENTIFIER, Token.Type.BOOLEAN, Token.Type.STRING, Token.Type.NUMBER, Token.Type.GROUP_BEGIN);

        Returnable<?> expression;
        if(id.isConstant()) {
            expression = parseConstantExpression(tokens);
        } else if(id.getType().equals(Token.Type.GROUP_BEGIN)) { // Parse grouped expression
            expression = parseGroup(tokens, variableMap);
        } else {
            if(functions.containsKey(id.getContent()) || builtinFunctions.contains(id.getContent()))
                expression = parseFunction(tokens, false, variableMap);
            else if(variableMap.containsKey(id.getContent())) {
                ParserUtil.checkType(tokens.consume(), Token.Type.IDENTIFIER);
                expression = new Getter(variableMap.get(id.getContent()));
            } else throw new ParseException("Unexpected token \" " + id.getContent() + "\"", id.getPosition());
        }

        if(booleanInverted) { // Invert operation if boolean not detected
            ParserUtil.checkReturnType(expression, Returnable.ReturnType.BOOLEAN);
            expression = new BooleanNotOperation((Returnable<Boolean>) expression, expression.getPosition());
        }

        if(full && tokens.get().isBinaryOperator()) { // Parse binary operations
            return parseBinaryOperation(expression, tokens, variableMap);
        }
        return expression;
    }

    private ConstantExpression<?> parseConstantExpression(TokenHolder tokens) throws ParseException {
        Token constantToken = tokens.consume();
        Position position = constantToken.getPosition();
        switch(constantToken.getType()) {
            case NUMBER:
                String content = constantToken.getContent();
                return new NumericConstant(content.contains(".") ? Double.parseDouble(content) : Integer.parseInt(content), position);
            case STRING:
                return new StringConstant(constantToken.getContent(), position);
            case BOOLEAN:
                return new BooleanConstant(Boolean.parseBoolean(constantToken.getContent()), position);
            default:
                throw new UnsupportedOperationException("Unsupported constant token: " + constantToken.getType() + " at position: " + position);
        }
    }

    private Returnable<?> parseGroup(TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN);
        Returnable<?> expression = parseExpression(tokens, true, variableMap); // Parse inside of group as a separate expression
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        return expression;
    }


    private BinaryOperation<?, ?> parseBinaryOperation(Returnable<?> left, TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        Token binaryOperator = tokens.consume();
        ParserUtil.checkBinaryOperator(binaryOperator);

        Returnable<?> right = parseExpression(tokens, false, variableMap);

        Token other = tokens.get();
        if(other.isBinaryOperator() && (other.getType().equals(Token.Type.MULTIPLICATION_OPERATOR) || other.getType().equals(Token.Type.DIVISION_OPERATOR))) {
            return assemble(left, parseBinaryOperation(right, tokens, variableMap), binaryOperator);
        } else if(other.isBinaryOperator()) {
            return parseBinaryOperation(assemble(left, right, binaryOperator), tokens, variableMap);
        }
        return assemble(left, right, binaryOperator);
    }

    private BinaryOperation<?, ?> assemble(Returnable<?> left, Returnable<?> right, Token binaryOperator) throws ParseException {
        if(binaryOperator.isStrictNumericOperator())
            ParserUtil.checkArithmeticOperation(left, right, binaryOperator); // Numeric type checking
        if(binaryOperator.isStrictBooleanOperator()) ParserUtil.checkBooleanOperation(left, right, binaryOperator); // Boolean type checking
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

    private Variable<?> parseVariableDeclaration(TokenHolder tokens, Returnable.ReturnType type) throws ParseException {
        ParserUtil.checkVarType(tokens.get(), type); // Check for type mismatch
        switch(type) {
            case NUMBER:
                return new NumberVariable(0d, tokens.get().getPosition());
            case STRING:
                return new StringVariable("", tokens.get().getPosition());
            case BOOLEAN:
                return new BooleanVariable(false, tokens.get().getPosition());
        }
        throw new UnsupportedOperationException("Unsupported variable type: " + type);
    }

    private Block parseBlock(TokenHolder tokens, Map<String, Variable<?>> superVars) throws ParseException {
        List<Item<?>> parsedItems = new GlueList<>();

        Map<String, Variable<?>> parsedVariables = new HashMap<>(superVars); // New hashmap as to not mutate parent scope's declarations.

        Token first = tokens.get();

        while(tokens.hasNext()) {
            Token token = tokens.get();
            if(token.getType().equals(Token.Type.BLOCK_END)) break; // Stop parsing at block end.
            parsedItems.add(parseItem(tokens, parsedVariables));
            if(tokens.hasNext() && !token.isLoopLike()) ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        }
        return new Block(parsedItems, first.getPosition());
    }

    private Item<?> parseItem(TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        Token token = tokens.get();
        ParserUtil.checkType(token, Token.Type.IDENTIFIER, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP,
                Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.RETURN, Token.Type.BREAK, Token.Type.CONTINUE, Token.Type.FAIL);

        if(token.isLoopLike()) { // Parse loop-like tokens (if, while, etc)
            return parseLoopLike(tokens, variableMap);
        } else if(token.isIdentifier()) { // Parse identifiers
            if(variableMap.containsKey(token.getContent())) { // Assume variable assignment
                Variable<?> variable = variableMap.get(token.getContent());
                return parseAssignment(variable, tokens, variableMap);
            } else return parseFunction(tokens, true, variableMap);
        } else if(token.isVariableDeclaration()) {
            Variable<?> temp = parseVariableDeclaration(tokens, ParserUtil.getVariableReturnType(token));

            ParserUtil.checkType(tokens.consume(), Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.NUMBER_VARIABLE);
            Token name = tokens.get();
            ParserUtil.checkType(name, Token.Type.IDENTIFIER); // Name must be an identifier.

            if(functions.containsKey(name.getContent()) || variableMap.containsKey(name.getContent()) || builtinFunctions.contains(name.getContent()))
                throw new ParseException(name.getContent() + " is already defined in this scope", name.getPosition());

            variableMap.put(name.getContent(), temp);


            return parseAssignment(temp, tokens, variableMap);
        } else if(token.getType().equals(Token.Type.RETURN)) return new ReturnKeyword(tokens.consume().getPosition());
        else if(token.getType().equals(Token.Type.BREAK)) return new BreakKeyword(tokens.consume().getPosition());
        else if(token.getType().equals(Token.Type.CONTINUE)) return new ContinueKeyword(tokens.consume().getPosition());
        else if(token.getType().equals(Token.Type.FAIL)) return new FailKeyword(tokens.consume().getPosition());
        else throw new UnsupportedOperationException("Unexpected token " + token.getType() + ": " + token.getPosition());
    }

    private Assignment<?> parseAssignment(Variable<?> variable, TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        Token name = tokens.get();

        ParserUtil.checkType(tokens.consume(), Token.Type.IDENTIFIER);

        ParserUtil.checkType(tokens.consume(), Token.Type.ASSIGNMENT);

        Returnable<?> expression = parseExpression(tokens, true, variableMap);

        ParserUtil.checkReturnType(expression, variable.getType());

        return new Assignment<>((Variable<Object>) variable, (Returnable<Object>) expression, name.getPosition());
    }

    private Function<?> parseFunction(TokenHolder tokens, boolean fullStatement, Map<String, Variable<?>> variableMap) throws ParseException {
        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER); // First token must be identifier

        if(!functions.containsKey(identifier.getContent()) && !builtinFunctions.contains(identifier.getContent()))
            throw new ParseException("No such function \"" + identifier.getContent() + "\"", identifier.getPosition());

        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN); // Second is body begin


        List<Returnable<?>> args = getArgs(tokens, variableMap); // Extract arguments, consume the rest.

        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END); // Remove body end

        if(fullStatement) ParserUtil.checkType(tokens.get(), Token.Type.STATEMENT_END);

        if(functions.containsKey(identifier.getContent())) {
            FunctionBuilder<?> builder = functions.get(identifier.getContent());

            if(builder.argNumber() != -1 && args.size() != builder.argNumber())
                throw new ParseException("Expected " + builder.argNumber() + " arguments, found " + args.size(), identifier.getPosition());

            for(int i = 0; i < args.size(); i++) {
                Returnable<?> argument = args.get(i);
                if(builder.getArgument(i) == null)
                    throw new ParseException("Unexpected argument at position " + i + " in function " + identifier.getContent(), identifier.getPosition());
                ParserUtil.checkReturnType(argument, builder.getArgument(i));
            }
            return builder.build(args, identifier.getPosition());
        } else {
            switch(identifier.getContent()) {
                case "abs":
                    ParserUtil.checkReturnType(args.get(0), Returnable.ReturnType.NUMBER);
                    if(args.size() != 1)
                        throw new ParseException("Expected 1 argument; found " + args.size(), identifier.getPosition());
                    return new AbsFunction(identifier.getPosition(), (Returnable<Number>) args.get(0));
                case "sqrt":
                    ParserUtil.checkReturnType(args.get(0), Returnable.ReturnType.NUMBER);
                    if(args.size() != 1)
                        throw new ParseException("Expected 1 argument; found " + args.size(), identifier.getPosition());
                    return new SqrtFunction(identifier.getPosition(), (Returnable<Number>) args.get(0));
                case "pow":
                    ParserUtil.checkReturnType(args.get(0), Returnable.ReturnType.NUMBER);
                    ParserUtil.checkReturnType(args.get(1), Returnable.ReturnType.NUMBER);
                    if(args.size() != 2)
                        throw new ParseException("Expected 1 argument; found " + args.size(), identifier.getPosition());
                    return new PowFunction(identifier.getPosition(), (Returnable<Number>) args.get(0), (Returnable<Number>) args.get(1));
                default:
                    throw new UnsupportedOperationException("Unsupported function: " + identifier.getContent());
            }
        }
    }


    private List<Returnable<?>> getArgs(TokenHolder tokens, Map<String, Variable<?>> variableMap) throws ParseException {
        List<Returnable<?>> args = new GlueList<>();

        while(!tokens.get().getType().equals(Token.Type.GROUP_END)) {
            args.add(parseExpression(tokens, true, variableMap));
            ParserUtil.checkType(tokens.get(), Token.Type.SEPARATOR, Token.Type.GROUP_END);
            if(tokens.get().getType().equals(Token.Type.SEPARATOR)) tokens.consume();
        }
        return args;
    }
}
