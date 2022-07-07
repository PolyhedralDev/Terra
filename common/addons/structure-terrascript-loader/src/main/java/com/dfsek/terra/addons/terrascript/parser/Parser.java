/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Executable;
import com.dfsek.terra.addons.terrascript.parser.lang.Item;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope.ScopeBuilder;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.NumericConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.StringConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.BreakKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.ContinueKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.FailKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.ReturnKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike.ForKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike.IfKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike.WhileKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BooleanAndOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BooleanNotOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BooleanOrOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.ConcatenationOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.DivisionOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.ModuloOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.MultiplicationOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.NegationOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.NumberAdditionOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.SubtractionOperation;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.statements.EqualsStatement;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.statements.GreaterOrEqualsThanStatement;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.statements.GreaterThanStatement;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.statements.LessThanOrEqualsStatement;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.statements.LessThanStatement;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.statements.NotEqualsStatement;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.assign.BoolAssignmentNode;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.assign.NumAssignmentNode;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.assign.StrAssignmentNode;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.assign.VariableAssignmentNode;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.reference.BoolVariableReferenceNode;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.reference.NumVariableReferenceNode;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.reference.StrVariableReferenceNode;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.addons.terrascript.tokenizer.Token;
import com.dfsek.terra.addons.terrascript.tokenizer.Tokenizer;
import com.dfsek.terra.api.util.generic.pair.Pair;


@SuppressWarnings("unchecked")
public class Parser {
    private final String data;
    private final Map<String, FunctionBuilder<? extends Function<?>>> functions = new HashMap<>();
    private final List<String> ignoredFunctions = new ArrayList<>();
    
    public Parser(String data) {
        this.data = data;
    }
    
    public Parser registerFunction(String name, FunctionBuilder<? extends Function<?>> functionBuilder) {
        functions.put(name, functionBuilder);
        return this;
    }
    
    public Parser ignoreFunction(String name) {
        ignoredFunctions.add(name);
        return this;
    }
    
    /**
     * Parse input
     *
     * @return executable {@link Block}
     *
     * @throws ParseException If parsing fails.
     */
    public Executable parse() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        return new Executable(parseBlock(new Tokenizer(data), false, scopeBuilder), scopeBuilder);
    }
    
    private Keyword<?> parseLoopLike(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) throws ParseException {
        
        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN);
        
        return switch(identifier.getType()) {
            case FOR_LOOP -> parseForLoop(tokens, identifier.getPosition(), scopeBuilder);
            case IF_STATEMENT -> parseIfStatement(tokens, identifier.getPosition(), loop, scopeBuilder);
            case WHILE_LOOP -> parseWhileLoop(tokens, identifier.getPosition(), scopeBuilder);
            default -> throw new UnsupportedOperationException(
                    "Unknown keyword " + identifier.getContent() + ": " + identifier.getPosition());
        };
    }
    
    private WhileKeyword parseWhileLoop(Tokenizer tokens, Position start, ScopeBuilder scopeBuilder) {
        Returnable<?> first = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(first, Returnable.ReturnType.BOOLEAN);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        
        return new WhileKeyword(parseStatementBlock(tokens, true, scopeBuilder), (Returnable<Boolean>) first, start); // While loop
    }
    
    private IfKeyword parseIfStatement(Tokenizer tokens, Position start, boolean loop, ScopeBuilder scopeBuilder) {
        Returnable<?> condition = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(condition, Returnable.ReturnType.BOOLEAN);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        
        Block elseBlock = null;
        Block statement = parseStatementBlock(tokens, loop, scopeBuilder);
        
        List<Pair<Returnable<Boolean>, Block>> elseIf = new ArrayList<>();
        
        while(tokens.hasNext() && tokens.get().getType().equals(Token.Type.ELSE)) {
            tokens.consume(); // Consume else.
            if(tokens.get().getType().equals(Token.Type.IF_STATEMENT)) {
                tokens.consume(); // Consume if.
                Returnable<?> elseCondition = parseExpression(tokens, true, scopeBuilder);
                ParserUtil.checkReturnType(elseCondition, Returnable.ReturnType.BOOLEAN);
                elseIf.add(Pair.of((Returnable<Boolean>) elseCondition, parseStatementBlock(tokens, loop, scopeBuilder)));
            } else {
                elseBlock = parseStatementBlock(tokens, loop, scopeBuilder);
                break; // Else must be last.
            }
        }
        
        return new IfKeyword(statement, (Returnable<Boolean>) condition, elseIf, elseBlock, start); // If statement
    }
    
    private Block parseStatementBlock(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) {
        
        if(tokens.get().getType().equals(Token.Type.BLOCK_BEGIN)) {
            ParserUtil.checkType(tokens.consume(), Token.Type.BLOCK_BEGIN);
            Block block = parseBlock(tokens, loop, scopeBuilder);
            ParserUtil.checkType(tokens.consume(), Token.Type.BLOCK_END);
            return block;
        } else {
            Position position = tokens.get().getPosition();
            Block block = new Block(Collections.singletonList(parseItem(tokens, loop, scopeBuilder)), position);
            ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
            return block;
        }
    }
    
    private ForKeyword parseForLoop(Tokenizer tokens, Position start, ScopeBuilder scopeBuilder) {
        scopeBuilder = scopeBuilder.sub(); // new scope
        Token f = tokens.get();
        ParserUtil.checkType(f, Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.IDENTIFIER);
        Item<?> initializer;
        if(f.isVariableDeclaration()) {
            VariableAssignmentNode<?> forVar = parseVariableDeclaration(tokens, scopeBuilder);
            Token name = tokens.get();
            if(functions.containsKey(name.getContent()) || scopeBuilder.contains(name.getContent()))
                throw new ParseException(name.getContent() + " is already defined in this scope", name.getPosition());
            initializer = forVar;
        } else initializer = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        Returnable<?> conditional = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(conditional, Returnable.ReturnType.BOOLEAN);
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        
        Item<?> incrementer;
        Token token = tokens.get();
        if(scopeBuilder.contains(token.getContent())) { // Assume variable assignment
            incrementer = parseAssignment(tokens, scopeBuilder);
        } else incrementer = parseFunction(tokens, true, scopeBuilder);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        
        return new ForKeyword(parseStatementBlock(tokens, true, scopeBuilder), initializer, (Returnable<Boolean>) conditional, incrementer,
                              start);
    }
    
    private Returnable<?> parseExpression(Tokenizer tokens, boolean full, ScopeBuilder scopeBuilder) {
        boolean booleanInverted = false; // Check for boolean not operator
        boolean negate = false;
        if(tokens.get().getType().equals(Token.Type.BOOLEAN_NOT)) {
            booleanInverted = true;
            tokens.consume();
        } else if(tokens.get().getType().equals(Token.Type.SUBTRACTION_OPERATOR)) {
            negate = true;
            tokens.consume();
        }
        
        Token id = tokens.get();
        
        ParserUtil.checkType(id, Token.Type.IDENTIFIER, Token.Type.BOOLEAN, Token.Type.STRING, Token.Type.NUMBER, Token.Type.GROUP_BEGIN);
        
        Returnable<?> expression;
        if(id.isConstant()) {
            expression = parseConstantExpression(tokens);
        } else if(id.getType().equals(Token.Type.GROUP_BEGIN)) { // Parse grouped expression
            expression = parseGroup(tokens, scopeBuilder);
        } else {
            if(functions.containsKey(id.getContent()))
                expression = parseFunction(tokens, false, scopeBuilder);
            else if(scopeBuilder.contains(id.getContent())) {
                ParserUtil.checkType(tokens.consume(), Token.Type.IDENTIFIER);
                String varId = id.getContent();
                ReturnType varType = scopeBuilder.getType(varId);
                expression = switch(varType) {
                    case NUMBER -> new NumVariableReferenceNode(id.getPosition(), varType, scopeBuilder.getIndex(varId));
                    case STRING -> new StrVariableReferenceNode(id.getPosition(), varType, scopeBuilder.getIndex(varId));
                    case BOOLEAN -> new BoolVariableReferenceNode(id.getPosition(), varType, scopeBuilder.getIndex(varId));
                    default -> throw new ParseException("Illegal type for variable reference: " + varType, id.getPosition());
                };
                
            } else throw new ParseException("Unexpected token \" " + id.getContent() + "\"", id.getPosition());
        }
        
        if(booleanInverted) { // Invert operation if boolean not detected
            ParserUtil.checkReturnType(expression, Returnable.ReturnType.BOOLEAN);
            expression = new BooleanNotOperation((Returnable<Boolean>) expression, expression.getPosition());
        } else if(negate) {
            ParserUtil.checkReturnType(expression, Returnable.ReturnType.NUMBER);
            expression = new NegationOperation((Returnable<Number>) expression, expression.getPosition());
        }
        
        if(full && tokens.get().isBinaryOperator()) { // Parse binary operations
            return parseBinaryOperation(expression, tokens, scopeBuilder);
        }
        return expression;
    }
    
    private ConstantExpression<?> parseConstantExpression(Tokenizer tokens) {
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
                throw new UnsupportedOperationException(
                        "Unsupported constant token: " + constantToken.getType() + " at position: " + position);
        }
    }
    
    private Returnable<?> parseGroup(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN);
        Returnable<?> expression = parseExpression(tokens, true, scopeBuilder); // Parse inside of group as a separate expression
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        return expression;
    }
    
    private BinaryOperation<?, ?> parseBinaryOperation(Returnable<?> left, Tokenizer tokens,
                                                       ScopeBuilder scopeBuilder) {
        Token binaryOperator = tokens.consume();
        ParserUtil.checkBinaryOperator(binaryOperator);
        
        Returnable<?> right = parseExpression(tokens, false, scopeBuilder);
        
        Token other = tokens.get();
        if(ParserUtil.hasPrecedence(binaryOperator.getType(), other.getType())) {
            return assemble(left, parseBinaryOperation(right, tokens, scopeBuilder), binaryOperator);
        } else if(other.isBinaryOperator()) {
            return parseBinaryOperation(assemble(left, right, binaryOperator), tokens, scopeBuilder);
        }
        return assemble(left, right, binaryOperator);
    }
    
    private BinaryOperation<?, ?> assemble(Returnable<?> left, Returnable<?> right, Token binaryOperator) {
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
                return new GreaterOrEqualsThanStatement((Returnable<Number>) left, (Returnable<Number>) right,
                                                        binaryOperator.getPosition());
            case LESS_THAN_OR_EQUALS_OPERATOR:
                return new LessThanOrEqualsStatement((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            case BOOLEAN_AND:
                return new BooleanAndOperation((Returnable<Boolean>) left, (Returnable<Boolean>) right, binaryOperator.getPosition());
            case BOOLEAN_OR:
                return new BooleanOrOperation((Returnable<Boolean>) left, (Returnable<Boolean>) right, binaryOperator.getPosition());
            case MODULO_OPERATOR:
                return new ModuloOperation((Returnable<Number>) left, (Returnable<Number>) right, binaryOperator.getPosition());
            default:
                throw new UnsupportedOperationException("Unsupported binary operator: " + binaryOperator.getType());
        }
    }
    
    private VariableAssignmentNode<?> parseVariableDeclaration(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        Token type = tokens.consume();
        ParserUtil.checkType(type, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.NUMBER_VARIABLE);
        
        Returnable.ReturnType returnType = ParserUtil.getVariableReturnType(type);
        
        ParserUtil.checkVarType(type, returnType); // Check for type mismatch
        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER);
        if(functions.containsKey(identifier.getContent()) || scopeBuilder.contains(identifier.getContent()))
            throw new ParseException(identifier.getContent() + " is already defined in this scope", identifier.getPosition());
        ParserUtil.checkType(tokens.consume(), Token.Type.ASSIGNMENT);
        
        Returnable<?> value = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(value, returnType);

        String id = identifier.getContent();

        return switch(value.returnType()) {
            case NUMBER -> new NumAssignmentNode((Returnable<Number>) value, identifier.getPosition(), scopeBuilder.num(id));
            case STRING -> new StrAssignmentNode((Returnable<String>) value, identifier.getPosition(), scopeBuilder.str(id));
            case BOOLEAN -> new BoolAssignmentNode((Returnable<Boolean>) value, identifier.getPosition(), scopeBuilder.bool(id));
            default -> throw new ParseException("Illegal type for variable declaration: " + type, value.getPosition());
        };
    }
    
    private Block parseBlock(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) {
        List<Item<?>> parsedItems = new ArrayList<>();
        
        scopeBuilder = scopeBuilder.sub();
        
        Token first = tokens.get();
        
        while(tokens.hasNext()) {
            Token token = tokens.get();
            if(token.getType().equals(Token.Type.BLOCK_END)) break; // Stop parsing at block end.
            Item<?> parsedItem = parseItem(tokens, loop, scopeBuilder);
            if(parsedItem != Function.NULL) {
                parsedItems.add(parsedItem);
            }
            if(tokens.hasNext() && !token.isLoopLike()) ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        }
        return new Block(parsedItems, first.getPosition());
    }
    
    private Item<?> parseItem(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) {
        Token token = tokens.get();
        if(loop) ParserUtil.checkType(token, Token.Type.IDENTIFIER, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP,
                                      Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE,
                                      Token.Type.RETURN, Token.Type.BREAK, Token.Type.CONTINUE, Token.Type.FAIL);
        else ParserUtil.checkType(token, Token.Type.IDENTIFIER, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP,
                                  Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.RETURN,
                                  Token.Type.FAIL);
        
        if(token.isLoopLike()) { // Parse loop-like tokens (if, while, etc)
            return parseLoopLike(tokens, loop, scopeBuilder);
        } else if(token.isIdentifier()) { // Parse identifiers
            if(scopeBuilder.contains(token.getContent())) { // Assume variable assignment
                return parseAssignment(tokens, scopeBuilder);
            } else return parseFunction(tokens, true, scopeBuilder);
        } else if(token.isVariableDeclaration()) {
            
            return parseVariableDeclaration(tokens, scopeBuilder);
            
        } else if(token.getType().equals(Token.Type.RETURN)) return new ReturnKeyword(tokens.consume().getPosition());
        else if(token.getType().equals(Token.Type.BREAK)) return new BreakKeyword(tokens.consume().getPosition());
        else if(token.getType().equals(Token.Type.CONTINUE)) return new ContinueKeyword(tokens.consume().getPosition());
        else if(token.getType().equals(Token.Type.FAIL)) return new FailKeyword(tokens.consume().getPosition());
        else throw new UnsupportedOperationException("Unexpected token " + token.getType() + ": " + token.getPosition());
    }
    
    private VariableAssignmentNode<?> parseAssignment(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        Token identifier = tokens.consume();
        
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.ASSIGNMENT);
        
        Returnable<?> value = parseExpression(tokens, true, scopeBuilder);
        
        String id = identifier.getContent();
        
        ParserUtil.checkReturnType(value, scopeBuilder.getType(id));
        
        ReturnType type = value.returnType();
        
        return switch(type) {
            case NUMBER -> new NumAssignmentNode((Returnable<Number>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            case STRING -> new StrAssignmentNode((Returnable<String>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            case BOOLEAN -> new BoolAssignmentNode((Returnable<Boolean>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            default -> throw new ParseException("Illegal type for variable assignment: " + type, value.getPosition());
        };
    }
    
    private Function<?> parseFunction(Tokenizer tokens, boolean fullStatement, ScopeBuilder scopeBuilder) {
        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER); // First token must be identifier
        
        if(!functions.containsKey(identifier.getContent()))
            throw new ParseException("No such function \"" + identifier.getContent() + "\"", identifier.getPosition());
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN); // Second is body begin
        
        
        List<Returnable<?>> args = getArgs(tokens, scopeBuilder); // Extract arguments, consume the rest.
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END); // Remove body end
        
        if(fullStatement) ParserUtil.checkType(tokens.get(), Token.Type.STATEMENT_END);
        
        if(ignoredFunctions.contains(identifier.getContent())) {
            return Function.NULL;
        }
        
        if(functions.containsKey(identifier.getContent())) {
            FunctionBuilder<?> builder = functions.get(identifier.getContent());
            
            if(builder.argNumber() != -1 && args.size() != builder.argNumber())
                throw new ParseException("Expected " + builder.argNumber() + " arguments, found " + args.size(), identifier.getPosition());
            
            for(int i = 0; i < args.size(); i++) {
                Returnable<?> argument = args.get(i);
                if(builder.getArgument(i) == null)
                    throw new ParseException("Unexpected argument at position " + i + " in function " + identifier.getContent(),
                                             identifier.getPosition());
                ParserUtil.checkReturnType(argument, builder.getArgument(i));
            }
            return builder.build(args, identifier.getPosition());
        }
        throw new UnsupportedOperationException("Unsupported function: " + identifier.getContent());
    }
    
    private List<Returnable<?>> getArgs(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        List<Returnable<?>> args = new ArrayList<>();
        
        while(!tokens.get().getType().equals(Token.Type.GROUP_END)) {
            args.add(parseExpression(tokens, true, scopeBuilder));
            ParserUtil.checkType(tokens.get(), Token.Type.SEPARATOR, Token.Type.GROUP_END);
            if(tokens.get().getType().equals(Token.Type.SEPARATOR)) tokens.consume();
        }
        return args;
    }
}
