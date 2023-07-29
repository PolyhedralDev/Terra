/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.dfsek.terra.addons.terrascript.lexer.Lexer;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.lexer.Token;
import com.dfsek.terra.addons.terrascript.lexer.Token.TokenType;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Executable;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope.ScopeBuilder;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.NumericConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.StringConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.UserDefinedFunctionBuilder;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.BreakKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.ContinueKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.FailKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow.ReturnKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike.ForKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike.IfKeyword;
import com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike.WhileKeyword;
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
import com.dfsek.terra.api.util.generic.pair.Pair;


public class Parser {
    private final List<String> ignoredFunctions = new ArrayList<>();
    
    private final Lexer lexer;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    /**
     * Parse input
     *
     * @return executable {@link Block}
     *
     * @throws ParseException If parsing fails.
     */
    public Executable parse(ScopeBuilder scopeBuilder) {
        return new Executable(parseBlock(scopeBuilder, ReturnType.VOID), scopeBuilder);
    }
    
    private WhileKeyword parseWhileLoop(ScopeBuilder scopeBuilder) {
        SourcePosition start = lexer.consume().getPosition();
        ParserUtil.ensureType(lexer.consume(), TokenType.OPEN_PAREN);
        scopeBuilder = scopeBuilder.innerLoopScope();
        Expression<?> condition = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(condition, Expression.ReturnType.BOOLEAN);
        ParserUtil.ensureType(lexer.consume(), TokenType.CLOSE_PAREN);
        return new WhileKeyword(parseStatementBlock(scopeBuilder, ReturnType.VOID), (Expression<Boolean>) condition,
                                start); // While loop
    }
    
    private IfKeyword parseIfStatement(ScopeBuilder scopeBuilder) {
        SourcePosition start = lexer.consume().getPosition();
        ParserUtil.ensureType(lexer.consume(), TokenType.OPEN_PAREN);
        Expression<?> condition = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(condition, Expression.ReturnType.BOOLEAN);
        
        ParserUtil.ensureType(lexer.consume(), TokenType.CLOSE_PAREN);
        
        Block elseBlock = null;
        Block statement = parseStatementBlock(scopeBuilder, ReturnType.VOID);
        
        List<Pair<Expression<Boolean>, Block>> elseIf = new ArrayList<>();
        
        while(lexer.hasNext() && lexer.current().isType(TokenType.ELSE)) {
            lexer.consume(); // Consume else.
            if(lexer.current().isType(TokenType.IF_STATEMENT)) {
                lexer.consume(); // Consume if.
                Expression<?> elseCondition = parseExpression(scopeBuilder);
                ParserUtil.ensureReturnType(elseCondition, Expression.ReturnType.BOOLEAN);
                elseIf.add(Pair.of((Expression<Boolean>) elseCondition, parseStatementBlock(scopeBuilder, ReturnType.VOID)));
            } else {
                elseBlock = parseStatementBlock(scopeBuilder, ReturnType.VOID);
                break; // Else must be last.
            }
        }
        
        return new IfKeyword(statement, (Expression<Boolean>) condition, elseIf, elseBlock, start); // If statement
    }
    
    private Block parseStatementBlock(ScopeBuilder scopeBuilder, ReturnType blockReturnType) {
        if(lexer.current().isType(TokenType.BLOCK_BEGIN)) {
            ParserUtil.ensureType(lexer.consume(), TokenType.BLOCK_BEGIN);
            Block block = parseBlock(scopeBuilder, blockReturnType);
            ParserUtil.ensureType(lexer.consume(), TokenType.BLOCK_END);
            return block;
        } else {
            SourcePosition position = lexer.current().getPosition();
            return new Block(Collections.singletonList(parseStatement(lexer, scopeBuilder)), position, blockReturnType);
        }
    }
    
    private ForKeyword parseForLoop(ScopeBuilder scopeBuilder) {
        SourcePosition start = lexer.consume().getPosition();
        ParserUtil.ensureType(lexer.consume(), TokenType.OPEN_PAREN);
        scopeBuilder = scopeBuilder.innerLoopScope(); // new scope
        Token f = lexer.current();
        ParserUtil.ensureType(f, TokenType.TYPE_NUMBER, TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN, TokenType.IDENTIFIER);
        Expression<?> initializer;
        if(f.isVariableDeclaration()) {
            Expression<?> forVar = parseDeclaration(scopeBuilder);
            Token name = lexer.current();
            if(scopeBuilder.containsFunction(name.getContent()) || scopeBuilder.containsVariable(name.getContent()))
                throw new ParseException(name.getContent() + " is already defined in this scope", name.getPosition());
            initializer = forVar;
        } else initializer = parseExpression(scopeBuilder);
        ParserUtil.ensureType(lexer.consume(), TokenType.STATEMENT_END);
        Expression<?> conditional = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(conditional, Expression.ReturnType.BOOLEAN);
        ParserUtil.ensureType(lexer.consume(), TokenType.STATEMENT_END);
        
        Expression<?> incrementer;
        Token incrementerToken = lexer.consume();
        if(scopeBuilder.containsVariable(incrementerToken.getContent())) { // Assume variable assignment
            incrementer = parseAssignment(incrementerToken, scopeBuilder);
        } else incrementer = parseFunctionInvocation(true, incrementerToken, scopeBuilder);
        
        ParserUtil.ensureType(lexer.consume(), TokenType.CLOSE_PAREN);
        
        return new ForKeyword(parseStatementBlock(scopeBuilder, ReturnType.VOID), initializer, (Expression<Boolean>) conditional,
                              incrementer,
                              start);
    }
    
    private Expression<?> parseExpression(ScopeBuilder scopeBuilder) {
        return parseLogicOr(scopeBuilder);
    }
    
    private Expression<?> parseLogicOr(ScopeBuilder scopeBuilder) {
        return parseLeftAssociativeBinaryOperation(this::parseLogicAnd, scopeBuilder, (op) -> {
            ParserUtil.ensureReturnType(op.left, ReturnType.BOOLEAN);
            ParserUtil.ensureReturnType(op.right, ReturnType.BOOLEAN);
        }, Map.of(TokenType.BOOLEAN_OR, (op) -> new BooleanOrOperation((Expression<Boolean>) op.left, (Expression<Boolean>) op.right, op.operator.getPosition())));
    }
    
    private Expression<?> parseLogicAnd(ScopeBuilder scopeBuilder) {
        return parseLeftAssociativeBinaryOperation(this::parseEquality, scopeBuilder, (op) -> {
            ParserUtil.ensureReturnType(op.left, ReturnType.BOOLEAN);
            ParserUtil.ensureReturnType(op.right, ReturnType.BOOLEAN);
        }, Map.of(TokenType.BOOLEAN_AND, (op) -> new BooleanAndOperation((Expression<Boolean>) op.left, (Expression<Boolean>) op.right, op.operator.getPosition())));
    }
    
    private Expression<?> parseEquality(ScopeBuilder scopeBuilder) {
        return parseLeftAssociativeBinaryOperation(this::parseComparison, scopeBuilder, Map.of(
            TokenType.EQUALS_EQUALS, (op) -> new EqualsStatement((Expression<Object>) op.left, (Expression<Object>) op.right, op.operator.getPosition()),
            TokenType.BANG_EQUALS, (op) -> new NotEqualsStatement((Expression<Object>) op.left, (Expression<Object>) op.right, op.operator.getPosition())
        ));
    }
    
    private Expression<?> parseComparison(ScopeBuilder scopeBuilder) {
        return parseLeftAssociativeBinaryOperation(this::parseTerm, scopeBuilder, (op) -> {
            ParserUtil.ensureReturnType(op.left, ReturnType.NUMBER);
            ParserUtil.ensureReturnType(op.right, ReturnType.NUMBER);
        }, Map.of(
            TokenType.LESS, (op) -> new LessThanStatement((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition()),
            TokenType.LESS_EQUALS, (op) -> new LessThanOrEqualsStatement((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition()),
            TokenType.GREATER, (op) -> new GreaterThanStatement((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition()),
            TokenType.GREATER_EQUAL, (op) -> new GreaterOrEqualsThanStatement((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition())
        ));
    }
    
    private Expression<?> parseTerm(ScopeBuilder scopeBuilder) {
        return parseLeftAssociativeBinaryOperation(this::parseFactor, scopeBuilder, Map.of(
        TokenType.MINUS, (op) -> {
            ParserUtil.ensureReturnType(op.left, ReturnType.NUMBER);
            ParserUtil.ensureReturnType(op.right, ReturnType.NUMBER);
            return new SubtractionOperation((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition());
        },
        TokenType.PLUS, (op) -> {
            if (op.left.returnType() == ReturnType.NUMBER && op.right.returnType() == ReturnType.NUMBER)
                return new NumberAdditionOperation((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition());
            else
                return new ConcatenationOperation((Expression<Object>) op.left, (Expression<Object>) op.right, op.operator.getPosition());
        }));
    }
    
    private Expression<?> parseFactor(ScopeBuilder scopeBuilder) {
        return parseLeftAssociativeBinaryOperation(this::parseUnary, scopeBuilder, (op) -> {
           ParserUtil.ensureReturnType(op.left, ReturnType.NUMBER);
           ParserUtil.ensureReturnType(op.right, ReturnType.NUMBER);
        }, Map.of(
            TokenType.STAR, (op) -> new MultiplicationOperation((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition()),
            TokenType.FORWARD_SLASH, (op) -> new DivisionOperation((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition()),
            TokenType.MODULO_OPERATOR, (op) -> new ModuloOperation((Expression<Number>) op.left, (Expression<Number>) op.right, op.operator.getPosition())
        ));
    }
    
    private Expression<?> parseUnary(ScopeBuilder scopeBuilder) {
        if (lexer.current().isType(TokenType.BANG, TokenType.MINUS)) {
            Token operator = lexer.consume();
            Expression<?> right = parseUnary(scopeBuilder);
            return switch(operator.getType()) {
                case BANG -> {
                    ParserUtil.ensureReturnType(right, ReturnType.BOOLEAN);
                    yield new BooleanNotOperation((Expression<Boolean>) right, operator.getPosition());
                }
                case MINUS -> {
                    ParserUtil.ensureReturnType(right, ReturnType.NUMBER);
                    yield new NegationOperation((Expression<Number>) right, operator.getPosition());
                }
                default -> throw new IllegalStateException();
            };
        }
        return parsePrimary(scopeBuilder);
    }
    
    private Expression<?> parsePrimary(ScopeBuilder scopeBuilder) {
        Token token = lexer.consume();
        return switch(token.getType()) {
            case NUMBER -> {
                String content = token.getContent();
                yield new NumericConstant(content.contains(".") ? Double.parseDouble(content) : Integer.parseInt(content), token.getPosition());
            }
            case STRING -> new StringConstant(token.getContent(), token.getPosition());
            case BOOLEAN -> new BooleanConstant(Boolean.parseBoolean(token.getContent()), token.getPosition());
            case OPEN_PAREN -> {
                Expression<?> expr = parseExpression(scopeBuilder);
                ParserUtil.ensureType(lexer.consume(), TokenType.CLOSE_PAREN);
                yield expr;
            }
            case IDENTIFIER -> {
                if (scopeBuilder.containsFunction(token.getContent()))
                    yield parseFunctionInvocation(false, token, scopeBuilder);
                else if (scopeBuilder.containsVariable(token.getContent())) {
                    ReturnType variableType = scopeBuilder.getVaraibleType(token.getContent());
                    yield switch(variableType) {
                        case NUMBER -> new NumVariableReferenceNode(token.getPosition(), variableType, scopeBuilder.getIndex(token.getContent()));
                        case BOOLEAN -> new BoolVariableReferenceNode(token.getPosition(), variableType, scopeBuilder.getIndex(token.getContent()));
                        case STRING -> new StrVariableReferenceNode(token.getPosition(), variableType, scopeBuilder.getIndex(token.getContent()));
                        default -> throw new ParseException("Illegal type for variable reference: " + variableType, token.getPosition());
                    };
                }
                throw new ParseException("Identifier " + token.getContent() + " is not defined in this scope", token.getPosition());
            }
            default -> throw new ParseException("Unexpected token " + token.getType(), token.getPosition());
        };
    }
    
    private Expression<?> parseLeftAssociativeBinaryOperation(Function<ScopeBuilder, Expression<?>> higherPrecedence, ScopeBuilder scopeBuilder,
                                                              Consumer<BinaryOperationInfo> init,
                                                              Map<TokenType, Function<BinaryOperationInfo, Expression<?>>> operators) {
        Expression<?> expr = higherPrecedence.apply(scopeBuilder);
        TokenType[] opTypes = operators.keySet().toArray(new TokenType[0]);
        while (lexer.current().isType(opTypes)) {
            Token operator = lexer.consume();
            Expression<?> right = higherPrecedence.apply(scopeBuilder);
            BinaryOperationInfo op = new BinaryOperationInfo(expr, operator, right);
            init.accept(op);
            expr = operators.get(operator.getType()).apply(op);
        }
        return expr;
    }
    
    private Expression<?> parseLeftAssociativeBinaryOperation(Function<ScopeBuilder, Expression<?>> higherPrecedence, ScopeBuilder scopeBuilder, Map<TokenType, Function<BinaryOperationInfo, Expression<?>>> operators) {
        return parseLeftAssociativeBinaryOperation(higherPrecedence, scopeBuilder, (op) -> {}, operators);
    }
    
    private record BinaryOperationInfo(Expression<?> left, Token operator, Expression<?> right) {}
    
    private Expression<?> parseDeclaration(ScopeBuilder scopeBuilder) {
        Token type = lexer.consume();
        
        Token identifier = lexer.consume();
        ParserUtil.ensureType(identifier, TokenType.IDENTIFIER);
        
        Token declarationType = lexer.consume();
        ParserUtil.ensureType(declarationType, TokenType.ASSIGNMENT, TokenType.OPEN_PAREN);
        
        return switch(declarationType.getType()) {
            case ASSIGNMENT -> parseVariableDeclaration(scopeBuilder, type, identifier);
            case OPEN_PAREN -> parseFunctionDeclaration(scopeBuilder, type, identifier);
            default -> throw new ParseException("Illegal type for declaration: " + type, declarationType.getPosition());
        };
    }
    
    
    private Expression<?> parseVariableDeclaration(ScopeBuilder scopeBuilder, Token type, Token identifier) {
        ParserUtil.ensureType(type, TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN, TokenType.TYPE_NUMBER);
        
        if(scopeBuilder.containsVariable(identifier.getContent()))
            throw new ParseException(identifier.getContent() + " is already defined in this scope", identifier.getPosition());
        
        Expression<?> value = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(value, ParserUtil.getVariableReturnType(type));
        
        String variableName = identifier.getContent();
        return switch(value.returnType()) {
            case NUMBER -> new NumAssignmentNode((Expression<Number>) value, identifier.getPosition(),
                                                 scopeBuilder.declareNum(variableName));
            case STRING -> new StrAssignmentNode((Expression<String>) value, identifier.getPosition(),
                                                 scopeBuilder.declareStr(variableName));
            case BOOLEAN -> new BoolAssignmentNode((Expression<Boolean>) value, identifier.getPosition(),
                                                   scopeBuilder.declareBool(variableName));
            default -> throw new ParseException("Illegal type for variable declaration: " + type, value.getPosition());
        };
    }
    
    private Expression<?> parseFunctionDeclaration(ScopeBuilder scopeBuilder, Token type, Token identifier) {
        ParserUtil.ensureType(type, TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN, TokenType.TYPE_NUMBER, TokenType.TYPE_VOID);
        
        if(scopeBuilder.containsVariable(identifier.getContent()))
            throw new ParseException(identifier.getContent() + " is already defined in this scope", identifier.getPosition());
        
        ReturnType returnType = ParserUtil.getVariableReturnType(type);
        
        ScopeBuilder functionBodyScope = scopeBuilder.functionScope();
        
        // Declare argument names into function body scope
        List<Pair<Integer, ReturnType>> argumentInfo = getFunctionArgumentsDeclaration().stream().map(
                arg -> Pair.of(switch(arg.getRight()) {
                    case NUMBER -> functionBodyScope.declareNum(arg.getLeft());
                    case BOOLEAN -> functionBodyScope.declareBool(arg.getLeft());
                    case STRING -> functionBodyScope.declareStr(arg.getLeft());
                    default -> throw new IllegalArgumentException("Unsupported argument type: " + arg.getRight());
                }, arg.getRight())).toList();
        
        Block body = parseStatementBlock(functionBodyScope, returnType);
        
        FunctionBuilder<?> functionBuilder = new UserDefinedFunctionBuilder<>(returnType, argumentInfo, body, functionBodyScope);
        
        scopeBuilder.registerFunction(identifier.getContent(), functionBuilder);
        return Expression.NOOP;
    }
    
    private List<Pair<String, ReturnType>> getFunctionArgumentsDeclaration() {
        List<Pair<String, ReturnType>> arguments = new ArrayList<>();
        while(lexer.current().getType() != TokenType.CLOSE_PAREN) {
            // Parse argument type
            Token typeToken = lexer.consume();
            ParserUtil.ensureType(typeToken, TokenType.TYPE_BOOLEAN, TokenType.TYPE_STRING, TokenType.TYPE_NUMBER);
            ReturnType argType = ParserUtil.getVariableReturnType(typeToken);
            
            // Parse argument name
            Token identifierToken = lexer.consume();
            ParserUtil.ensureType(identifierToken, TokenType.IDENTIFIER);
            String argName = identifierToken.getContent();
            
            arguments.add(Pair.of(argName, argType));
            
            // Consume separator if present, trailing separators are allowed
            if(lexer.current().isType(TokenType.SEPARATOR)) lexer.consume();
        }
        ParserUtil.ensureType(lexer.consume(), TokenType.CLOSE_PAREN);
        return arguments;
    }
    
    private Block parseBlock(ScopeBuilder scopeBuilder, ReturnType blockReturnType) {
        List<Expression<?>> expressions = new ArrayList<>();
        scopeBuilder = scopeBuilder.innerScope(); // Create new inner scope for the block
        SourcePosition startPosition = lexer.current().getPosition();
        
        boolean hasReturn = false;
        
        // Parse each statement
        while(lexer.hasNext() && !lexer.current().isType(TokenType.BLOCK_END)) {
            Expression<?> expression = parseStatement(lexer, scopeBuilder);
            if(expression != Expression.NOOP) {
                expressions.add(expression);
            }
            if(expression instanceof ReturnKeyword returnKeyword) {
                hasReturn = true;
                if(returnKeyword.dataReturnType() != blockReturnType)
                    throw new ParseException(
                            "Invalid return type, expected " + blockReturnType + ", found " + returnKeyword.dataReturnType(),
                            expression.getPosition());
            }
        }
        
        if(blockReturnType != ReturnType.VOID && !hasReturn)
            throw new ParseException("Block does not contain a return statement, must return type " + blockReturnType, startPosition);
        
        return new Block(expressions, startPosition, blockReturnType);
    }
    
    private Expression<?> parseStatement(Lexer lexer, ScopeBuilder scopeBuilder) {
        Token token = lexer.current();
        
        // Include BREAK and CONTINUE as valid token types if scope is within a loop
        if(scopeBuilder.isInLoop()) ParserUtil.ensureType(token, TokenType.IDENTIFIER, TokenType.IF_STATEMENT, TokenType.WHILE_LOOP,
                                                          TokenType.FOR_LOOP,
                                                          TokenType.TYPE_NUMBER, TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN,
                                                          TokenType.TYPE_VOID,
                                                          TokenType.RETURN, TokenType.BREAK, TokenType.CONTINUE, TokenType.FAIL);
        else ParserUtil.ensureType(token, TokenType.IDENTIFIER, TokenType.IF_STATEMENT, TokenType.WHILE_LOOP, TokenType.FOR_LOOP,
                                   TokenType.TYPE_NUMBER, TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN, TokenType.TYPE_VOID,
                                   TokenType.RETURN,
                                   TokenType.FAIL);
        
        Expression<?> expression = switch(token.getType()) {
            case FOR_LOOP -> parseForLoop(scopeBuilder);
            case IF_STATEMENT -> parseIfStatement(scopeBuilder);
            case WHILE_LOOP -> parseWhileLoop(scopeBuilder);
            case IDENTIFIER -> {
                if(scopeBuilder.containsVariable(token.getContent())) yield parseAssignment(lexer.consume(), scopeBuilder); // Assume variable assignment
                else yield parseFunctionInvocation(true, lexer.consume(), scopeBuilder);
            }
            case TYPE_NUMBER, TYPE_STRING, TYPE_BOOLEAN, TYPE_VOID -> parseDeclaration(scopeBuilder);
            case RETURN -> parseReturn(scopeBuilder);
            case BREAK -> new BreakKeyword(lexer.consume().getPosition());
            case CONTINUE -> new ContinueKeyword(lexer.consume().getPosition());
            case FAIL -> new FailKeyword(lexer.consume().getPosition());
            default -> throw new UnsupportedOperationException("Unexpected token " + token.getType() + ": " + token.getPosition());
        };
        if(!token.isControlStructure() && expression != Expression.NOOP) ParserUtil.ensureType(lexer.consume(), TokenType.STATEMENT_END);
        return expression;
    }
    
    private ReturnKeyword parseReturn(ScopeBuilder scopeBuilder) {
        Token returnToken = lexer.consume();
        ParserUtil.ensureType(returnToken, TokenType.RETURN);
        Expression<?> data = null;
        if(!lexer.current().isType(TokenType.STATEMENT_END)) {
            data = parseExpression(scopeBuilder);
        }
        return new ReturnKeyword(data, returnToken.getPosition());
    }
    
    private VariableAssignmentNode<?> parseAssignment(Token identifier, ScopeBuilder scopeBuilder) {
        ParserUtil.ensureType(identifier, TokenType.IDENTIFIER);
        
        ParserUtil.ensureType(lexer.consume(), TokenType.ASSIGNMENT);
        
        Expression<?> value = parseExpression(scopeBuilder);
        
        String id = identifier.getContent();
        
        ParserUtil.ensureReturnType(value, scopeBuilder.getVaraibleType(id));
        
        ReturnType type = value.returnType();
        
        return switch(type) {
            case NUMBER -> new NumAssignmentNode((Expression<Number>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            case STRING -> new StrAssignmentNode((Expression<String>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            case BOOLEAN -> new BoolAssignmentNode((Expression<Boolean>) value, identifier.getPosition(), scopeBuilder.getIndex(id));
            default -> throw new ParseException("Illegal type for variable assignment: " + type, value.getPosition());
        };
    }
    
    private Expression<?> parseFunctionInvocation(boolean fullStatement, Token identifier, ScopeBuilder scopeBuilder) {
        if(!scopeBuilder.containsFunction(identifier.getContent()))
            throw new ParseException("Function \"" + identifier.getContent() + "\" is not defined in this scope", identifier.getPosition());
        
        ParserUtil.ensureType(lexer.consume(), TokenType.OPEN_PAREN); // Invocation starts with open paren
        
        List<Expression<?>> args = getFunctionArgs(scopeBuilder); // Extract arguments, consume the rest.
        
        ParserUtil.ensureType(lexer.consume(), TokenType.CLOSE_PAREN); // Remove body end
        
        if(fullStatement) ParserUtil.ensureType(lexer.current(), TokenType.STATEMENT_END);
        
        if(ignoredFunctions.contains(identifier.getContent())) {
            return Expression.NOOP;
        }
        
        if(scopeBuilder.containsFunction(identifier.getContent())) {
            FunctionBuilder<?> builder = scopeBuilder.getFunction(identifier.getContent());
            
            if(builder.argNumber() != -1 && args.size() != builder.argNumber())
                throw new ParseException("Expected " + builder.argNumber() + " arguments, found " + args.size(), identifier.getPosition());
            
            for(int i = 0; i < args.size(); i++) {
                Expression<?> argument = args.get(i);
                if(builder.getArgument(i) == null)
                    throw new ParseException("Unexpected argument at position " + i + " in function " + identifier.getContent(),
                                             identifier.getPosition());
                ParserUtil.ensureReturnType(argument, builder.getArgument(i));
            }
            return builder.build(args, identifier.getPosition());
        }
        throw new UnsupportedOperationException("Unsupported function: " + identifier.getContent());
    }
    
    private List<Expression<?>> getFunctionArgs(ScopeBuilder scopeBuilder) {
        List<Expression<?>> args = new ArrayList<>();
        
        while(!lexer.current().isType(TokenType.CLOSE_PAREN)) {
            args.add(parseExpression(scopeBuilder));
            ParserUtil.ensureType(lexer.current(), TokenType.SEPARATOR, TokenType.CLOSE_PAREN);
            if(lexer.current().isType(TokenType.SEPARATOR)) lexer.consume();
        }
        return args;
    }
}
