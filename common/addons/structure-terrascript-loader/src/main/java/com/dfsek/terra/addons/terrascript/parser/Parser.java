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
    
    private WhileKeyword parseWhileLoop(Tokenizer tokenizer, SourcePosition start, ScopeBuilder scopeBuilder) {
        Expression<?> first = parseLogicMathExpression(tokenizer, true, scopeBuilder);
        ParserUtil.checkReturnType(first, Expression.ReturnType.BOOLEAN);
        
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_END);
        
        return new WhileKeyword(parseStatementBlock(tokenizer, true, scopeBuilder), (Expression<Boolean>) first, start); // While loop
    }
    
    private IfKeyword parseIfStatement(Tokenizer tokenizer, SourcePosition start, boolean controlStructure, ScopeBuilder scopeBuilder) {
        Expression<?> condition = parseLogicMathExpression(tokenizer, true, scopeBuilder);
        ParserUtil.checkReturnType(condition, Expression.ReturnType.BOOLEAN);
        
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_END);
        
        Block elseBlock = null;
        Block statement = parseStatementBlock(tokenizer, controlStructure, scopeBuilder);
        
        List<Pair<Expression<Boolean>, Block>> elseIf = new ArrayList<>();
        
        while(tokenizer.hasNext() && tokenizer.current().isType(Token.Type.ELSE)) {
            tokenizer.consume(); // Consume else.
            if(tokenizer.current().isType(Token.Type.IF_STATEMENT)) {
                tokenizer.consume(); // Consume if.
                Expression<?> elseCondition = parseLogicMathExpression(tokenizer, true, scopeBuilder);
                ParserUtil.checkReturnType(elseCondition, Expression.ReturnType.BOOLEAN);
                elseIf.add(Pair.of((Expression<Boolean>) elseCondition, parseStatementBlock(tokenizer, controlStructure, scopeBuilder)));
            } else {
                elseBlock = parseStatementBlock(tokenizer, controlStructure, scopeBuilder);
                break; // Else must be last.
            }
        }
        
        return new IfKeyword(statement, (Expression<Boolean>) condition, elseIf, elseBlock, start); // If statement
    }
    
    private Block parseStatementBlock(Tokenizer tokenizer, boolean controlStructure, ScopeBuilder scopeBuilder) {
        
        if(tokenizer.current().isType(Token.Type.BLOCK_BEGIN)) {
            ParserUtil.ensureType(tokenizer.consume(), Token.Type.BLOCK_BEGIN);
            Block block = parseBlock(tokenizer, controlStructure, scopeBuilder);
            ParserUtil.ensureType(tokenizer.consume(), Token.Type.BLOCK_END);
            return block;
        } else {
            SourcePosition position = tokenizer.current().getPosition();
            Block block = new Block(Collections.singletonList(parseExpression(tokenizer, controlStructure, scopeBuilder)), position);
            ParserUtil.ensureType(tokenizer.consume(), Token.Type.STATEMENT_END);
            return block;
        }
    }
    
    private ForKeyword parseForLoop(Tokenizer tokenizer, SourcePosition start, ScopeBuilder scopeBuilder) {
        scopeBuilder = scopeBuilder.sub(); // new scope
        Token f = tokenizer.current();
        ParserUtil.ensureType(f, Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.IDENTIFIER);
        Expression<?> initializer;
        if(f.isVariableDeclaration()) {
            VariableAssignmentNode<?> forVar = parseVariableDeclaration(tokenizer, scopeBuilder);
            Token name = tokenizer.current();
            if(functions.containsKey(name.getContent()) || scopeBuilder.contains(name.getContent()))
                throw new ParseException(name.getContent() + " is already defined in this scope", name.getPosition());
            initializer = forVar;
        } else initializer = parseLogicMathExpression(tokenizer, true, scopeBuilder);
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.STATEMENT_END);
        Expression<?> conditional = parseLogicMathExpression(tokenizer, true, scopeBuilder);
        ParserUtil.checkReturnType(conditional, Expression.ReturnType.BOOLEAN);
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.STATEMENT_END);
        
        Expression<?> incrementer;
        Token token = tokenizer.current();
        if(scopeBuilder.contains(token.getContent())) { // Assume variable assignment
            incrementer = parseAssignment(tokenizer, scopeBuilder);
        } else incrementer = parseFunction(tokenizer, true, scopeBuilder);
        
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_END);
        
        return new ForKeyword(parseStatementBlock(tokenizer, true, scopeBuilder), initializer, (Expression<Boolean>) conditional, incrementer,
                              start);
    }
    
    private Expression<?> parseLogicMathExpression(Tokenizer tokenizer, boolean full, ScopeBuilder scopeBuilder) {
        boolean booleanInverted = false; // Check for boolean not operator
        boolean negate = false;
        if(tokenizer.current().isType(Token.Type.BOOLEAN_NOT)) {
            booleanInverted = true;
            tokenizer.consume();
        } else if(tokenizer.current().isType(Token.Type.SUBTRACTION_OPERATOR)) {
            negate = true;
            tokenizer.consume();
        }
        
        Token id = tokenizer.current();
        
        ParserUtil.ensureType(id, Token.Type.IDENTIFIER, Token.Type.BOOLEAN, Token.Type.STRING, Token.Type.NUMBER, Token.Type.GROUP_BEGIN);
        
        Expression<?> expression;
        if(id.isConstant()) {
            expression = parseConstantExpression(tokenizer);
        } else if(id.isType(Token.Type.GROUP_BEGIN)) { // Parse grouped expression
            expression = parseGroup(tokenizer, scopeBuilder);
        } else {
            if(functions.containsKey(id.getContent()))
                expression = parseFunction(tokenizer, false, scopeBuilder);
            else if(scopeBuilder.contains(id.getContent())) {
                ParserUtil.ensureType(tokenizer.consume(), Token.Type.IDENTIFIER);
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
        
        if(full && tokenizer.current().isBinaryOperator()) { // Parse binary operations
            return parseBinaryOperation(expression, tokenizer, scopeBuilder);
        }
        return expression;
    }
    
    private ConstantExpression<?> parseConstantExpression(Tokenizer tokenizer) {
        Token constantToken = tokenizer.consume();
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
    
    private Expression<?> parseGroup(Tokenizer tokenizer, ScopeBuilder scopeBuilder) {
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_BEGIN);
        Expression<?> expression = parseLogicMathExpression(tokenizer, true, scopeBuilder); // Parse inside of group as a separate expression
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_END);
        return expression;
    }
    
    private BinaryOperation<?, ?> parseBinaryOperation(Expression<?> left, Tokenizer tokenizer,
                                                       ScopeBuilder scopeBuilder) {
        Token binaryOperator = tokenizer.consume();
        ParserUtil.checkBinaryOperator(binaryOperator);
        
        Expression<?> right = parseLogicMathExpression(tokenizer, false, scopeBuilder);
        
        Token other = tokenizer.current();
        if(ParserUtil.hasPrecedence(binaryOperator.getType(), other.getType())) {
            return assemble(left, parseBinaryOperation(right, tokenizer, scopeBuilder), binaryOperator);
        } else if(other.isBinaryOperator()) {
            return parseBinaryOperation(assemble(left, right, binaryOperator), tokenizer, scopeBuilder);
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
    
    private VariableAssignmentNode<?> parseVariableDeclaration(Tokenizer tokenizer, ScopeBuilder scopeBuilder) {
        Token type = tokenizer.consume();
        ParserUtil.ensureType(type, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.NUMBER_VARIABLE);
        
        Expression.ReturnType returnType = ParserUtil.getVariableReturnType(type);
        
        ParserUtil.checkVarType(type, returnType); // Check for type mismatch
        Token identifier = tokenizer.consume();
        ParserUtil.ensureType(identifier, Token.Type.IDENTIFIER);
        if(functions.containsKey(identifier.getContent()) || scopeBuilder.contains(identifier.getContent()))
            throw new ParseException(identifier.getContent() + " is already defined in this scope", identifier.getPosition());
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.ASSIGNMENT);
        
        Expression<?> value = parseLogicMathExpression(tokenizer, true, scopeBuilder);
        ParserUtil.checkReturnType(value, returnType);

        String id = identifier.getContent();

        return switch(value.returnType()) {
            case NUMBER -> new NumAssignmentNode((Expression<Number>) value, identifier.getPosition(), scopeBuilder.num(id));
            case STRING -> new StrAssignmentNode((Expression<String>) value, identifier.getPosition(), scopeBuilder.str(id));
            case BOOLEAN -> new BoolAssignmentNode((Expression<Boolean>) value, identifier.getPosition(), scopeBuilder.bool(id));
            default -> throw new ParseException("Illegal type for variable declaration: " + type, value.getPosition());
        };
    }
    
    private Block parseBlock(Tokenizer tokenizer, boolean controlStructure, ScopeBuilder scopeBuilder) {
        List<Expression<?>> expressions = new ArrayList<>();
        
        scopeBuilder = scopeBuilder.sub();
        
        Token first = tokenizer.current();
        
        while(tokenizer.hasNext()) {
            Token token = tokenizer.current();
            if(token.isType(Token.Type.BLOCK_END)) break; // Stop parsing at block end.
            Expression<?> expression = parseExpression(tokenizer, controlStructure, scopeBuilder);
            if(expression != Function.NULL) {
                expressions.add(expression);
            }
            if(tokenizer.hasNext() && !token.isControlStructure()) ParserUtil.ensureType(tokenizer.consume(), Token.Type.STATEMENT_END);
        }
        return new Block(expressions, first.getPosition());
    }
    
    private Expression<?> parseExpression(Tokenizer tokenizer, boolean controlStructure, ScopeBuilder scopeBuilder) {
        Token token = tokenizer.current();
        if(controlStructure) ParserUtil.ensureType(token, Token.Type.IDENTIFIER, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP,
                                       Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE,
                                       Token.Type.RETURN, Token.Type.BREAK, Token.Type.CONTINUE, Token.Type.FAIL);
        else ParserUtil.ensureType(token, Token.Type.IDENTIFIER, Token.Type.IF_STATEMENT, Token.Type.WHILE_LOOP, Token.Type.FOR_LOOP,
                                   Token.Type.NUMBER_VARIABLE, Token.Type.STRING_VARIABLE, Token.Type.BOOLEAN_VARIABLE, Token.Type.RETURN,
                                   Token.Type.FAIL);
        
        Expression<?> expression = switch(token.getType()) {
            case FOR_LOOP, IF_STATEMENT, WHILE_LOOP -> {
                Token identifier = tokenizer.consume();
                ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_BEGIN);
                yield switch(identifier.getType()) {
                    case FOR_LOOP -> parseForLoop(tokenizer, identifier.getPosition(), scopeBuilder);
                    case IF_STATEMENT -> parseIfStatement(tokenizer, identifier.getPosition(), controlStructure, scopeBuilder);
                    case WHILE_LOOP -> parseWhileLoop(tokenizer, identifier.getPosition(), scopeBuilder);
                    default -> throw new UnsupportedOperationException("Unknown keyword " + identifier.getContent() + ": " + identifier.getPosition());
                };
            }
            case IDENTIFIER -> {
                if(scopeBuilder.contains(token.getContent())) yield parseAssignment(tokenizer, scopeBuilder); // Assume variable assignment
                else yield parseFunction(tokenizer, true, scopeBuilder);
            }
            case NUMBER_VARIABLE, STRING_VARIABLE, BOOLEAN_VARIABLE -> parseVariableDeclaration(tokenizer, scopeBuilder);
            case RETURN -> new ReturnKeyword(tokenizer.consume().getPosition());
            case BREAK -> new BreakKeyword(tokenizer.consume().getPosition());
            case CONTINUE -> new ContinueKeyword(tokenizer.consume().getPosition());
            case FAIL -> new FailKeyword(tokenizer.consume().getPosition());
            default -> throw new UnsupportedOperationException("Unexpected token " + token.getType() + ": " + token.getPosition());
        };
        return expression;
    }
    
    private VariableAssignmentNode<?> parseAssignment(Tokenizer tokenizer, ScopeBuilder scopeBuilder) {
        Token identifier = tokenizer.consume();
        
        ParserUtil.ensureType(identifier, Token.Type.IDENTIFIER);
        
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.ASSIGNMENT);
        
        Expression<?> value = parseLogicMathExpression(tokenizer, true, scopeBuilder);
        
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
    
    private Function<?> parseFunction(Tokenizer tokenizer, boolean fullStatement, ScopeBuilder scopeBuilder) {
        Token identifier = tokenizer.consume();
        ParserUtil.ensureType(identifier, Token.Type.IDENTIFIER); // First token must be identifier
        
        if(!functions.containsKey(identifier.getContent()))
            throw new ParseException("No such function \"" + identifier.getContent() + "\"", identifier.getPosition());
        
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_BEGIN); // Second is body begin
        
        List<Expression<?>> args = getFunctionArgs(tokenizer, scopeBuilder); // Extract arguments, consume the rest.
        
        ParserUtil.ensureType(tokenizer.consume(), Token.Type.GROUP_END); // Remove body end
        
        if(fullStatement) ParserUtil.ensureType(tokenizer.current(), Token.Type.STATEMENT_END);
        
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
    
    private List<Expression<?>> getFunctionArgs(Tokenizer tokenizer, ScopeBuilder scopeBuilder) {
        List<Expression<?>> args = new ArrayList<>();
        
        while(!tokenizer.current().isType(Token.Type.GROUP_END)) {
            args.add(parseLogicMathExpression(tokenizer, true, scopeBuilder));
            ParserUtil.ensureType(tokenizer.current(), Token.Type.SEPARATOR, Token.Type.GROUP_END);
            if(tokenizer.current().isType(Token.Type.SEPARATOR)) tokenizer.consume();
        }
        return args;
    }
}
