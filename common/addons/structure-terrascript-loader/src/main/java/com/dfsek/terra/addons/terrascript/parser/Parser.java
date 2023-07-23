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
import com.dfsek.terra.addons.terrascript.parser.lang.Statement;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression.ReturnType;
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
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.addons.terrascript.tokenizer.Token;
import com.dfsek.terra.addons.terrascript.tokenizer.Tokenizer;
import com.dfsek.terra.api.util.generic.pair.Pair;


@SuppressWarnings("unchecked")
public class Parser {
    private final String source;
    private final Map<String, FunctionBuilder<? extends Function<?>>> functions = new HashMap<>();
    private final List<String> ignoredFunctions = new ArrayList<>();
    
    public Parser(String source) {
        this.source = source;
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
        return new Executable(parseBlock(new Tokenizer(source), false, scopeBuilder), scopeBuilder);
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
    
    private WhileKeyword parseWhileLoop(Tokenizer tokens, SourcePosition start, ScopeBuilder scopeBuilder) {
        Expression<?> first = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(first, Expression.ReturnType.BOOLEAN);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        
        return new WhileKeyword(parseStatementBlock(tokens, true, scopeBuilder), (Expression<Boolean>) first, start); // While loop
    }
    
    private IfKeyword parseIfStatement(Tokenizer tokens, SourcePosition start, boolean loop, ScopeBuilder scopeBuilder) {
        Expression<?> condition = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(condition, Expression.ReturnType.BOOLEAN);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        
        Block elseBlock = null;
        Block statement = parseStatementBlock(tokens, loop, scopeBuilder);
        
        List<Pair<Expression<Boolean>, Block>> elseIf = new ArrayList<>();
        
        while(tokens.hasNext() && tokens.current().getType().equals(Token.Type.ELSE)) {
            tokens.consume(); // Consume else.
            if(tokens.current().getType().equals(Token.Type.IF_STATEMENT)) {
                tokens.consume(); // Consume if.
                Expression<?> elseCondition = parseExpression(tokens, true, scopeBuilder);
                ParserUtil.checkReturnType(elseCondition, Expression.ReturnType.BOOLEAN);
                elseIf.add(Pair.of((Expression<Boolean>) elseCondition, parseStatementBlock(tokens, loop, scopeBuilder)));
            } else {
                elseBlock = parseStatementBlock(tokens, loop, scopeBuilder);
                break; // Else must be last.
            }
        }
        
        return new IfKeyword(statement, (Expression<Boolean>) condition, elseIf, elseBlock, start); // If statement
    }
    
    private Block parseStatementBlock(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) {
        
        if(tokens.current().getType().equals(Token.Type.BLOCK_BEGIN)) {
            ParserUtil.checkType(tokens.consume(), Token.Type.BLOCK_BEGIN);
            Block block = parseBlock(tokens, loop, scopeBuilder);
            ParserUtil.checkType(tokens.consume(), Token.Type.BLOCK_END);
            return block;
        } else {
            SourcePosition position = tokens.current().getPosition();
            Block block = new Block(Collections.singletonList(parseStatement(tokens, loop, scopeBuilder)), position);
            ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
            return block;
        }
    }
    
    private ForKeyword parseForLoop(Tokenizer tokens, SourcePosition start, ScopeBuilder scopeBuilder) {
        scopeBuilder = scopeBuilder.sub(); // new scope
        Token f = tokens.current();
        ParserUtil.checkType(f, Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.IDENTIFIER);
        Statement<?> initializer;
        if(f.isVariableDeclaration()) {
            VariableAssignmentNode<?> forVar = parseVariableDeclaration(tokens, scopeBuilder);
            Token name = tokens.current();
            if(functions.containsKey(name.getContent()) || scopeBuilder.contains(name.getContent()))
                throw new ParseException(name.getContent() + " is already defined in this scope", name.getPosition());
            initializer = forVar;
        } else initializer = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        Expression<?> conditional = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(conditional, Expression.ReturnType.BOOLEAN);
        ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        
        Statement<?> incrementer;
        Token token = tokens.current();
        if(scopeBuilder.contains(token.getContent())) { // Assume variable assignment
            incrementer = parseAssignment(tokens, scopeBuilder);
        } else incrementer = parseFunction(tokens, true, scopeBuilder);
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        
        return new ForKeyword(parseStatementBlock(tokens, true, scopeBuilder), initializer, (Expression<Boolean>) conditional, incrementer,
                              start);
    }
    
    private Expression<?> parseExpression(Tokenizer tokens, boolean full, ScopeBuilder scopeBuilder) {
        boolean booleanInverted = false; // Check for boolean not operator
        boolean negate = false;
        if(tokens.current().getType().equals(Token.Type.BOOLEAN_NOT)) {
            booleanInverted = true;
            tokens.consume();
        } else if(tokens.current().getType().equals(Token.Type.SUBTRACTION_OPERATOR)) {
            negate = true;
            tokens.consume();
        }
        
        Token id = tokens.current();
        
        ParserUtil.checkType(id, Token.Type.IDENTIFIER, Token.Type.BOOLEAN, Token.Type.STRING, Token.Type.NUMBER, Token.Type.GROUP_BEGIN);
        
        Expression<?> expression;
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
            ParserUtil.checkReturnType(expression, Expression.ReturnType.BOOLEAN);
            expression = new BooleanNotOperation((Expression<Boolean>) expression, expression.getPosition());
        } else if(negate) {
            ParserUtil.checkReturnType(expression, Expression.ReturnType.NUMBER);
            expression = new NegationOperation((Expression<Number>) expression, expression.getPosition());
        }
        
        if(full && tokens.current().isBinaryOperator()) { // Parse binary operations
            return parseBinaryOperation(expression, tokens, scopeBuilder);
        }
        return expression;
    }
    
    private ConstantExpression<?> parseConstantExpression(Tokenizer tokens) {
        Token constantToken = tokens.consume();
        SourcePosition position = constantToken.getPosition();
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
    
    private Expression<?> parseGroup(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN);
        Expression<?> expression = parseExpression(tokens, true, scopeBuilder); // Parse inside of group as a separate expression
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END);
        return expression;
    }
    
    private BinaryOperation<?, ?> parseBinaryOperation(Expression<?> left, Tokenizer tokens,
                                                       ScopeBuilder scopeBuilder) {
        Token binaryOperator = tokens.consume();
        ParserUtil.checkBinaryOperator(binaryOperator);
        
        Expression<?> right = parseExpression(tokens, false, scopeBuilder);
        
        Token other = tokens.current();
        if(ParserUtil.hasPrecedence(binaryOperator.getType(), other.getType())) {
            return assemble(left, parseBinaryOperation(right, tokens, scopeBuilder), binaryOperator);
        } else if(other.isBinaryOperator()) {
            return parseBinaryOperation(assemble(left, right, binaryOperator), tokens, scopeBuilder);
        }
        return assemble(left, right, binaryOperator);
    }
    
    private BinaryOperation<?, ?> assemble(Expression<?> left, Expression<?> right, Token binaryOperator) {
        if(binaryOperator.isStrictNumericOperator())
            ParserUtil.checkArithmeticOperation(left, right, binaryOperator); // Numeric type checking
        if(binaryOperator.isStrictBooleanOperator()) ParserUtil.checkBooleanOperation(left, right, binaryOperator); // Boolean type checking
        switch(binaryOperator.getType()) {
            case ADDITION_OPERATOR:
                if(left.returnType().equals(Expression.ReturnType.NUMBER) && right.returnType().equals(Expression.ReturnType.NUMBER)) {
                    return new NumberAdditionOperation((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
                }
                return new ConcatenationOperation((Expression<Object>) left, (Expression<Object>) right, binaryOperator.getPosition());
            case SUBTRACTION_OPERATOR:
                return new SubtractionOperation((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            case MULTIPLICATION_OPERATOR:
                return new MultiplicationOperation((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            case DIVISION_OPERATOR:
                return new DivisionOperation((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            case EQUALS_OPERATOR:
                return new EqualsStatement((Expression<Object>) left, (Expression<Object>) right, binaryOperator.getPosition());
            case NOT_EQUALS_OPERATOR:
                return new NotEqualsStatement((Expression<Object>) left, (Expression<Object>) right, binaryOperator.getPosition());
            case GREATER_THAN_OPERATOR:
                return new GreaterThanStatement((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            case LESS_THAN_OPERATOR:
                return new LessThanStatement((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            case GREATER_THAN_OR_EQUALS_OPERATOR:
                return new GreaterOrEqualsThanStatement((Expression<Number>) left, (Expression<Number>) right,
                                                        binaryOperator.getPosition());
            case LESS_THAN_OR_EQUALS_OPERATOR:
                return new LessThanOrEqualsStatement((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            case BOOLEAN_AND:
                return new BooleanAndOperation((Expression<Boolean>) left, (Expression<Boolean>) right, binaryOperator.getPosition());
            case BOOLEAN_OR:
                return new BooleanOrOperation((Expression<Boolean>) left, (Expression<Boolean>) right, binaryOperator.getPosition());
            case MODULO_OPERATOR:
                return new ModuloOperation((Expression<Number>) left, (Expression<Number>) right, binaryOperator.getPosition());
            default:
                throw new UnsupportedOperationException("Unsupported binary operator: " + binaryOperator.getType());
        }
    }
    
    private VariableAssignmentNode<?> parseVariableDeclaration(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        Token type = tokens.consume();
        ParserUtil.checkType(type, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.NUMBER_VARIABLE);
        
        Expression.ReturnType returnType = ParserUtil.getVariableReturnType(type);
        
        ParserUtil.checkVarType(type, returnType); // Check for type mismatch
        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER);
        if(functions.containsKey(identifier.getContent()) || scopeBuilder.contains(identifier.getContent()))
            throw new ParseException(identifier.getContent() + " is already defined in this scope", identifier.getPosition());
        ParserUtil.checkType(tokens.consume(), Token.Type.ASSIGNMENT);
        
        Expression<?> value = parseExpression(tokens, true, scopeBuilder);
        ParserUtil.checkReturnType(value, returnType);

        String id = identifier.getContent();

        return switch(value.returnType()) {
            case NUMBER -> new NumAssignmentNode((Expression<Number>) value, identifier.getPosition(), scopeBuilder.num(id));
            case STRING -> new StrAssignmentNode((Expression<String>) value, identifier.getPosition(), scopeBuilder.str(id));
            case BOOLEAN -> new BoolAssignmentNode((Expression<Boolean>) value, identifier.getPosition(), scopeBuilder.bool(id));
            default -> throw new ParseException("Illegal type for variable declaration: " + type, value.getPosition());
        };
    }
    
    private Block parseBlock(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) {
        List<Statement<?>> statements = new ArrayList<>();
        
        scopeBuilder = scopeBuilder.sub();
        
        Token first = tokens.current();
        
        while(tokens.hasNext()) {
            Token token = tokens.current();
            if(token.getType().equals(Token.Type.BLOCK_END)) break; // Stop parsing at block end.
            Statement<?> statement = parseStatement(tokens, loop, scopeBuilder);
            if(statement != Function.NULL) {
                statements.add(statement);
            }
            if(tokens.hasNext() && !token.isLoopLike()) ParserUtil.checkType(tokens.consume(), Token.Type.STATEMENT_END);
        }
        return new Block(statements, first.getPosition());
    }
    
    private Statement<?> parseStatement(Tokenizer tokens, boolean loop, ScopeBuilder scopeBuilder) {
        Token token = tokens.current();
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
        
        Expression<?> value = parseExpression(tokens, true, scopeBuilder);
        
        String id = identifier.getContent();
        
        ParserUtil.checkReturnType(value, scopeBuilder.getType(id));
        
        ReturnType type = value.returnType();
        
        return switch(type) {
            case NUMBER -> new NumAssignmentNode((Expression<Number>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            case STRING -> new StrAssignmentNode((Expression<String>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            case BOOLEAN -> new BoolAssignmentNode((Expression<Boolean>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            default -> throw new ParseException("Illegal type for variable assignment: " + type, value.getPosition());
        };
    }
    
    private Function<?> parseFunction(Tokenizer tokens, boolean fullStatement, ScopeBuilder scopeBuilder) {
        Token identifier = tokens.consume();
        ParserUtil.checkType(identifier, Token.Type.IDENTIFIER); // First token must be identifier
        
        if(!functions.containsKey(identifier.getContent()))
            throw new ParseException("No such function \"" + identifier.getContent() + "\"", identifier.getPosition());
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_BEGIN); // Second is body begin
        
        
        List<Expression<?>> args = getArgs(tokens, scopeBuilder); // Extract arguments, consume the rest.
        
        ParserUtil.checkType(tokens.consume(), Token.Type.GROUP_END); // Remove body end
        
        if(fullStatement) ParserUtil.checkType(tokens.current(), Token.Type.STATEMENT_END);
        
        if(ignoredFunctions.contains(identifier.getContent())) {
            return Function.NULL;
        }
        
        if(functions.containsKey(identifier.getContent())) {
            FunctionBuilder<?> builder = functions.get(identifier.getContent());
            
            if(builder.argNumber() != -1 && args.size() != builder.argNumber())
                throw new ParseException("Expected " + builder.argNumber() + " arguments, found " + args.size(), identifier.getPosition());
            
            for(int i = 0; i < args.size(); i++) {
                Expression<?> argument = args.get(i);
                if(builder.getArgument(i) == null)
                    throw new ParseException("Unexpected argument at position " + i + " in function " + identifier.getContent(),
                                             identifier.getPosition());
                ParserUtil.checkReturnType(argument, builder.getArgument(i));
            }
            return builder.build(args, identifier.getPosition());
        }
        throw new UnsupportedOperationException("Unsupported function: " + identifier.getContent());
    }
    
    private List<Expression<?>> getArgs(Tokenizer tokens, ScopeBuilder scopeBuilder) {
        List<Expression<?>> args = new ArrayList<>();
        
        while(!tokens.current().getType().equals(Token.Type.GROUP_END)) {
            args.add(parseExpression(tokens, true, scopeBuilder));
            ParserUtil.checkType(tokens.current(), Token.Type.SEPARATOR, Token.Type.GROUP_END);
            if(tokens.current().getType().equals(Token.Type.SEPARATOR)) tokens.consume();
        }
        return args;
    }
}
