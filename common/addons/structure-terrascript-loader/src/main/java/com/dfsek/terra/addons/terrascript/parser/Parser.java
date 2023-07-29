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
        SourcePosition start = lexer.consume("Expected 'while' keyword at beginning of while loop", TokenType.WHILE_LOOP).getPosition();
        lexer.consume("Expected '(' proceeding 'while' keyword", TokenType.OPEN_PAREN);
        scopeBuilder = scopeBuilder.innerLoopScope();
        Expression<?> condition = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(condition, Expression.ReturnType.BOOLEAN);
        lexer.consume("Expected ')' proceeding while loop condition", TokenType.CLOSE_PAREN);
        return new WhileKeyword(parseStatementBlock(scopeBuilder, ReturnType.VOID), (Expression<Boolean>) condition,
                                start); // While loop
    }
    
    private IfKeyword parseIfStatement(ScopeBuilder scopeBuilder) {
        SourcePosition start = lexer.consume("Expected 'if' keyword at beginning of if statement", TokenType.IF_STATEMENT).getPosition();
        lexer.consume("Expected '(' proceeding 'if' keyword", TokenType.OPEN_PAREN);
        Expression<?> condition = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(condition, Expression.ReturnType.BOOLEAN);
        
        lexer.consume("Expected ')' proceeding if statement condition", TokenType.CLOSE_PAREN);
        
        Block elseBlock = null;
        Block statement = parseStatementBlock(scopeBuilder, ReturnType.VOID);
        
        List<Pair<Expression<Boolean>, Block>> elseIf = new ArrayList<>();
        
        while(lexer.hasNext() && lexer.current().isType(TokenType.ELSE)) {
            lexer.consumeUnchecked(); // Consume else.
            if(lexer.current().isType(TokenType.IF_STATEMENT)) {
                lexer.consumeUnchecked(); // Consume if.
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
            lexer.consumeUnchecked();
            Block block = parseBlock(scopeBuilder, blockReturnType);
            lexer.consume("Expected block end '}' after block statements", TokenType.BLOCK_END);
            return block;
        } else {
            SourcePosition position = lexer.current().getPosition();
            return new Block(Collections.singletonList(parseStatement(scopeBuilder)), position, blockReturnType);
        }
    }
    
    private ForKeyword parseForLoop(ScopeBuilder scopeBuilder) {
        SourcePosition start = lexer.consume("Expected 'for' keyword at beginning of for loop", TokenType.FOR_LOOP).getPosition();
        lexer.consume("Expected '(' after 'for' keyword", TokenType.OPEN_PAREN);
        scopeBuilder = scopeBuilder.innerLoopScope(); // new scope
        
        Expression<?> initializer = switch(lexer.current().getType()) {
            case TYPE_NUMBER, TYPE_STRING, TYPE_BOOLEAN -> {
                Token type = lexer.consume("Expected type before declaration", TokenType.TYPE_STRING, TokenType.TYPE_NUMBER, TokenType.TYPE_BOOLEAN, TokenType.TYPE_VOID);
                Token identifier = lexer.consume("Expected identifier after type", TokenType.IDENTIFIER);
                Expression<?> expr = parseVariableDeclaration(scopeBuilder, type, identifier);
                lexer.consume("Expected ';' after initializer within for loop", TokenType.STATEMENT_END);
                yield expr;
            }
            case IDENTIFIER -> {
                Expression<?> expr = parseAssignment(scopeBuilder);
                lexer.consume("Expected ';' after initializer within for loop", TokenType.STATEMENT_END);
                yield expr;
            }
            case STATEMENT_END -> {
                lexer.consumeUnchecked();
                yield Expression.NOOP;
            }
            default -> throw new ParseException("Unexpected token '" + lexer.current() + "', expected variable declaration or assignment", lexer.current().getPosition());
        };
        
        Expression<?> conditional;
        if (lexer.current().isType(TokenType.STATEMENT_END)) // If no conditional is provided, conditional defaults to true
            conditional = new BooleanConstant(true, lexer.current().getPosition());
        else
            conditional = parseExpression(scopeBuilder);
        ParserUtil.ensureReturnType(conditional, Expression.ReturnType.BOOLEAN);
        lexer.consume("Expected ';' separator after conditional within for loop", TokenType.STATEMENT_END);
        
        Expression<?> incrementer;
        if(lexer.current().isType(TokenType.CLOSE_PAREN))
            // If no incrementer is provided, do nothing
            incrementer = Expression.NOOP;
        else if(scopeBuilder.containsVariable(lexer.current().getContent())) // Assume variable assignment
            incrementer = parseAssignment(scopeBuilder);
        else
            incrementer = parseFunctionInvocation(lexer.consume("Expected function call within for loop incrementer", TokenType.IDENTIFIER), scopeBuilder);
        lexer.consume("Expected ')' after for loop incrementer", TokenType.CLOSE_PAREN);
        
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
            Token operator = lexer.consumeUnchecked();
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
        Token token = lexer.consumeUnchecked();
        return switch(token.getType()) {
            case NUMBER -> {
                String content = token.getContent();
                yield new NumericConstant(content.contains(".") ? Double.parseDouble(content) : Integer.parseInt(content), token.getPosition());
            }
            case STRING -> new StringConstant(token.getContent(), token.getPosition());
            case BOOLEAN -> new BooleanConstant(Boolean.parseBoolean(token.getContent()), token.getPosition());
            case OPEN_PAREN -> {
                Expression<?> expr = parseExpression(scopeBuilder);
                lexer.consume("Missing ')' at end of expression group", TokenType.CLOSE_PAREN);
                yield expr;
            }
            case IDENTIFIER -> {
                if (scopeBuilder.containsFunction(token.getContent()))
                    yield parseFunctionInvocation(token, scopeBuilder);
                else if (scopeBuilder.containsVariable(token.getContent())) {
                    ReturnType variableType = scopeBuilder.getVaraibleType(token.getContent());
                    yield switch(variableType) {
                        case NUMBER -> new NumVariableReferenceNode(token.getPosition(), variableType, scopeBuilder.getIndex(token.getContent()));
                        case BOOLEAN -> new BoolVariableReferenceNode(token.getPosition(), variableType, scopeBuilder.getIndex(token.getContent()));
                        case STRING -> new StrVariableReferenceNode(token.getPosition(), variableType, scopeBuilder.getIndex(token.getContent()));
                        default -> throw new ParseException("Illegal type for variable reference: " + variableType, token.getPosition());
                    };
                }
                throw new ParseException("Identifier '" + token.getContent() + "' is not defined in this scope", token.getPosition());
            }
            default -> throw new ParseException("Unexpected token '" + token.getContent() + "' when parsing expression", token.getPosition());
        };
    }
    
    private Expression<?> parseLeftAssociativeBinaryOperation(Function<ScopeBuilder, Expression<?>> higherPrecedence, ScopeBuilder scopeBuilder,
                                                              Consumer<BinaryOperationInfo> init,
                                                              Map<TokenType, Function<BinaryOperationInfo, Expression<?>>> operators) {
        Expression<?> expr = higherPrecedence.apply(scopeBuilder);
        TokenType[] opTypes = operators.keySet().toArray(new TokenType[0]);
        while (lexer.current().isType(opTypes)) {
            Token operator = lexer.consumeUnchecked();
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
        Token type = lexer.consume("Expected type before declaration", TokenType.TYPE_STRING, TokenType.TYPE_NUMBER, TokenType.TYPE_BOOLEAN, TokenType.TYPE_VOID);
        Token identifier = lexer.consume("Expected identifier after type", TokenType.IDENTIFIER);
        
        return switch(lexer.current().getType()) {
            case ASSIGNMENT -> parseVariableDeclaration(scopeBuilder, type, identifier);
            case OPEN_PAREN -> parseFunctionDeclaration(scopeBuilder, type, identifier);
            default -> throw new ParseException("Expected '=' for variable assignment or '(' for function declaration after identifier '" + identifier.getContent() + "'", lexer.current().getPosition());
        };
    }
    
    
    private Expression<?> parseVariableDeclaration(ScopeBuilder scopeBuilder, Token type, Token identifier) {
        lexer.consume("Expected '=' after identifier '" + identifier.getContent() + "' for variable declaration", TokenType.ASSIGNMENT);
        
        if (!type.isVariableDeclaration()) throw new ParseException("Expected type specification at beginning of variable declaration", type.getPosition());
        
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
        lexer.consume("Expected '(' after identifier '" + identifier.getContent() + "' for function declaration", TokenType.OPEN_PAREN);
        
        if(!(type.isType(TokenType.TYPE_STRING, TokenType.TYPE_BOOLEAN, TokenType.TYPE_NUMBER, TokenType.TYPE_VOID)))
            throw new ParseException("Invalid function declaration return type specification " + type.getType(), type.getPosition());
        
        if(scopeBuilder.containsVariable(identifier.getContent()))
            throw new ParseException(identifier.getContent() + " is already defined in this scope", identifier.getPosition());
        
        ReturnType returnType = ParserUtil.getVariableReturnType(type);
        
        ScopeBuilder functionBodyScope = scopeBuilder.functionScope();
        
        // Declare parameter names into function body scope
        List<Pair<Integer, ReturnType>> parameterInfo = getFunctionParameterDeclaration().stream().map(
                arg -> Pair.of(switch(arg.getRight()) {
                    case NUMBER -> functionBodyScope.declareNum(arg.getLeft());
                    case BOOLEAN -> functionBodyScope.declareBool(arg.getLeft());
                    case STRING -> functionBodyScope.declareStr(arg.getLeft());
                    default -> throw new IllegalArgumentException("Unsupported parameter type: " + arg.getRight());
                }, arg.getRight())).toList();
        
        Block body = parseStatementBlock(functionBodyScope, returnType);
        
        FunctionBuilder<?> functionBuilder = new UserDefinedFunctionBuilder<>(returnType, parameterInfo, body, functionBodyScope);
        
        scopeBuilder.registerFunction(identifier.getContent(), functionBuilder);
        return Expression.NOOP;
    }
    
    private List<Pair<String, ReturnType>> getFunctionParameterDeclaration() {
        List<Pair<String, ReturnType>> parameters = new ArrayList<>();
        while(lexer.current().getType() != TokenType.CLOSE_PAREN) {
            // Parse parameter type
            Token typeToken = lexer.consume("Expected function parameter type declaration", TokenType.TYPE_BOOLEAN, TokenType.TYPE_STRING, TokenType.TYPE_NUMBER);
            ReturnType type = ParserUtil.getVariableReturnType(typeToken);
            
            // Parse parameter name
            Token identifierToken = lexer.consume("Expected function parameter identifier", TokenType.IDENTIFIER);
            String name = identifierToken.getContent();
            
            parameters.add(Pair.of(name, type));
            
            // Consume separator if present, trailing separators are allowed
            if(lexer.current().isType(TokenType.SEPARATOR)) lexer.consumeUnchecked();
        }
        lexer.consume("Expected ')' after function parameter declaration", TokenType.CLOSE_PAREN);
        return parameters;
    }
    
    private Block parseBlock(ScopeBuilder scopeBuilder, ReturnType blockReturnType) {
        List<Expression<?>> expressions = new ArrayList<>();
        scopeBuilder = scopeBuilder.innerScope(); // Create new inner scope for the block
        SourcePosition startPosition = lexer.current().getPosition();
        
        boolean hasReturn = false;
        
        // Parse each statement
        while(lexer.hasNext() && !lexer.current().isType(TokenType.BLOCK_END)) {
            Expression<?> expression = parseStatement(scopeBuilder);
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
    
    private Expression<?> parseStatement(ScopeBuilder scopeBuilder) {
        Token token = lexer.current();
        Expression<?> expression = switch(token.getType()) {
            case FOR_LOOP -> parseForLoop(scopeBuilder);
            case IF_STATEMENT -> parseIfStatement(scopeBuilder);
            case WHILE_LOOP -> parseWhileLoop(scopeBuilder);
            case IDENTIFIER -> {
                if(scopeBuilder.containsVariable(token.getContent())) yield parseAssignment(scopeBuilder); // Assume variable assignment
                else yield parseFunctionInvocation(lexer.consumeUnchecked(), scopeBuilder);
            }
            case TYPE_NUMBER, TYPE_STRING, TYPE_BOOLEAN, TYPE_VOID -> parseDeclaration(scopeBuilder);
            case RETURN -> parseReturn(scopeBuilder);
            case BREAK -> {
                if (!scopeBuilder.isInLoop()) throw new ParseException("Break statements can only be defined inside loops", token.getPosition());
                yield new BreakKeyword(lexer.consumeUnchecked().getPosition());
            }
            case CONTINUE -> {
                if (!scopeBuilder.isInLoop()) throw new ParseException("Continue statements can only be defined inside loops", token.getPosition());
                yield new ContinueKeyword(lexer.consumeUnchecked().getPosition());
            }
            case FAIL -> new FailKeyword(lexer.consumeUnchecked().getPosition());
            case STATEMENT_END -> Expression.NOOP;
            default -> throw new UnsupportedOperationException("Unexpected token " + token.getType() + ": " + token.getPosition());
        };
        if(!token.isControlStructure() && expression != Expression.NOOP) lexer.consume("Expected ';' at end of statement", TokenType.STATEMENT_END);
        return expression;
    }
    
    private ReturnKeyword parseReturn(ScopeBuilder scopeBuilder) {
        Token returnToken = lexer.consume("Expected 'return' keyword at beginning of return statement", TokenType.RETURN);
        Expression<?> data = null;
        if(!lexer.current().isType(TokenType.STATEMENT_END)) {
            data = parseExpression(scopeBuilder);
        }
        return new ReturnKeyword(data, returnToken.getPosition());
    }
    
    private VariableAssignmentNode<?> parseAssignment(ScopeBuilder scopeBuilder) {
        Token identifier = lexer.consume("Expected identifier at beginning of assignment", TokenType.IDENTIFIER);
        
        lexer.consume("Expected '=' after identifier for variable assignment", TokenType.ASSIGNMENT);
        
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
    
    private Expression<?> parseFunctionInvocation(Token identifier, ScopeBuilder scopeBuilder) {
        if(!scopeBuilder.containsFunction(identifier.getContent()))
            throw new ParseException("Function '" + identifier.getContent() + "' is not defined in this scope", identifier.getPosition());
        
        FunctionBuilder<?> builder = scopeBuilder.getFunction(identifier.getContent());
        
        lexer.consume("Expected '(' after identifier " + identifier.getContent(), TokenType.OPEN_PAREN); // Invocation starts with open paren
        
        List<Expression<?>> args = new ArrayList<>();
        while(!lexer.current().isType(TokenType.CLOSE_PAREN)) {
            args.add(parseExpression(scopeBuilder));
            if (lexer.current().isType(TokenType.CLOSE_PAREN)) break;
            lexer.consume("Expected ',' between function arguments", TokenType.SEPARATOR);
        }
        lexer.consume("Expected ')' after function arguments", TokenType.CLOSE_PAREN);
        
        if(ignoredFunctions.contains(identifier.getContent())) {
            return Expression.NOOP;
        }
        
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
    
}
