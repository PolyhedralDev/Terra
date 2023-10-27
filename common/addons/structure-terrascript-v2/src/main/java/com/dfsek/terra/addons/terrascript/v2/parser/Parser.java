package com.dfsek.terra.addons.terrascript.v2.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import com.dfsek.terra.addons.terrascript.v2.Type;
import com.dfsek.terra.addons.terrascript.v2.Type.TypeException;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Variable;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.Block;
import com.dfsek.terra.addons.terrascript.v2.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.v2.lexer.Token;
import com.dfsek.terra.addons.terrascript.v2.lexer.Token.TokenType;
import com.dfsek.terra.api.util.generic.pair.Pair;


/**
 * TerraScript recursive descent parser
 */
public class Parser {
    
    private final List<Token> tokens;
    
    private int index = 0;
    
    private Parser(List<Token> tokens) {
        if(tokens.stream().noneMatch(t -> t.isType(TokenType.END_OF_FILE)))
            throw new IllegalArgumentException("Token list must contain at least one token of type " + TokenType.END_OF_FILE);
        this.tokens = tokens;
    }
    
    public static Block parse(List<Token> tokens) {
        return new Parser(tokens).parseTokens();
    }
    
    private Block parseTokens() {
        List<Stmt> statements = new ArrayList<>();
        while(hasNext()) {
            statements.add(statement());
        }
        if(hasNext()) throw new ParseException("Tokens were remaining after parsing", current().position());
        return new Stmt.Block(statements, new SourcePosition(0, 0));
    }
    
    private Token current() {
        return tokens.get(index);
    }
    
    private boolean hasNext() {
        return !current().isType(TokenType.END_OF_FILE);
    }
    
    private Token consume(String wrongTypeMessage, TokenType expected, TokenType... more) {
        if(!current().isType(expected) && Arrays.stream(more).noneMatch(t -> t == current().type())) throw new ParseException(
                wrongTypeMessage, current().position());
        return consumeUnchecked();
    }
    
    public Token consumeUnchecked() {
        if(!hasNext()) return current();
        Token temp = current();
        index++;
        return temp;
    }
    
    private void consumeStatementEnd(String after) {
        consume("Expected ';' after " + after + ", found '" + current().lexeme() + "'", TokenType.STATEMENT_END);
    }
    
    private Stmt statement() {
        return switch(current().type()) {
            case BLOCK_BEGIN -> block();
            case FUNCTION -> functionDeclaration();
            case VARIABLE -> variableDeclaration();
            case RETURN -> returnStmt();
            case IF_STATEMENT -> ifStmt();
            case FOR_LOOP -> forLoop();
            case WHILE_LOOP -> whileLoop();
            case BREAK -> breakStmt();
            case CONTINUE -> continueStmt();
            case STATEMENT_END -> new Stmt.NoOp(consumeUnchecked().position());
            default -> expressionStatement();
        };
    }
    
    private Stmt functionDeclaration() {
        SourcePosition position = consume("Expected 'fun' keyword at start of function declaration", TokenType.FUNCTION).position();
        String id = consume("Expected identifier after 'fun' keyword for function declaration", TokenType.IDENTIFIER).lexeme();
        consume("Expected '(' after function identifier '" + id + "'", TokenType.OPEN_PAREN);
        
        // Parse parameters
        List<Pair<String, Type>> params = new ArrayList<>();
        while(!current().isType(TokenType.CLOSE_PAREN)) {
            Token paramToken = consume("Expected parameter name or ')', found '" + current().lexeme() + "'", TokenType.IDENTIFIER);
            String paramId = paramToken.lexeme();
            if(params.stream().anyMatch(p -> Objects.equals(p.getLeft(), paramId)))
                throw new ParseException("Parameter '" + paramId + "' has already been declared in function '" + id + "'",
                                         paramToken.position());
            
            consume("Expected type declaration after parameter name. Example: '" + paramId + ": <type>'", TokenType.COLON);
            Type paramType = typeExpr();
            
            params.add(Pair.of(paramId, paramType));
            
            if(current().isType(TokenType.CLOSE_PAREN)) break;
            consume("Expected ',' or ')' after parameter declaration '" + paramId + "' in function '" + id + "'", TokenType.SEPARATOR);
        }
        
        Type funcReturn = Type.VOID;
        
        consume("Expected ')' after " + (params.size() == 0 ? "')'" : "parameters") + " in declaration of function '" + id + "'",
                TokenType.CLOSE_PAREN);
        if(current().isType(TokenType.COLON)) {
            consumeUnchecked();
            funcReturn = typeExpr();
        }
        
        Stmt.Block body = blockOrSingleStatement();
        
        return new Stmt.FunctionDeclaration(id, params, funcReturn, body, position);
    }
    
    private Stmt.VariableDeclaration variableDeclaration() {
        SourcePosition position = consume("Expected 'var' keyword at start of variable declaration", TokenType.VARIABLE).position();
        String id = consume("Expected variable name after type for variable declaration", TokenType.IDENTIFIER).lexeme();
        consume("Expected ':' after variable name", TokenType.COLON);
        Type type = typeExpr();
        consume("Expected '=' following variable type declaration", TokenType.ASSIGNMENT);
        Expr expr = expression();
        consumeStatementEnd("variable declaration");
        
        return new Stmt.VariableDeclaration(type, id, expr, position);
    }
    
    private Type typeExpr() {
        Token typeToken = consume("Expected " + TokenType.IDENTIFIER + " specified as variable type", TokenType.IDENTIFIER);
        try {
            return Type.fromString(typeToken.lexeme());
        } catch(TypeException e) {
            throw new ParseException("Failed to parse type expression", typeToken.position());
        }
    }
    
    private Stmt.Return returnStmt() {
        SourcePosition position = consume("Expected 'return' keyword, found '" + current().lexeme() + "'", TokenType.RETURN).position();
        Expr value = new Expr.Void(position);
        if(!current().isType(TokenType.STATEMENT_END))
            value = expression();
        consumeStatementEnd("return statement");
        return new Stmt.Return(value, position);
    }
    
    private Stmt.If ifStmt() {
        // Parse main if clause
        SourcePosition position = consume("Expected 'if' keyword at beginning of if statement", TokenType.IF_STATEMENT).position();
        consume("Expected '(' after 'if' keyword", TokenType.OPEN_PAREN);
        Expr condition = expression();
        consume("Expected ')' after if statement condition", TokenType.CLOSE_PAREN);
        Stmt.Block trueBody = blockOrSingleStatement();
        
        // Parse any else clauses
        Stmt.Block elseBody = null;
        List<Pair<Expr, Stmt.Block>> elseIfClauses = new ArrayList<>();
        while(current().isType(TokenType.ELSE)) {
            consumeUnchecked(); // Consume else
            
            if(!current().isType(TokenType.IF_STATEMENT)) {
                elseBody = blockOrSingleStatement();
                break; // Else clause should be last in if statement
            }
            
            consumeUnchecked(); // Consume if
            consume("Expected '(' after 'else if', e.g. 'if else (<condition>) ...'", TokenType.OPEN_PAREN);
            Expr elseIfCondition = expression();
            consume("Expected ')' after 'else if' clause, e.g. 'else if (<condition>) ...'", TokenType.CLOSE_PAREN);
            Stmt.Block elseIfBody = blockOrSingleStatement();
            elseIfClauses.add(Pair.of(elseIfCondition, elseIfBody));
        }
        
        return new Stmt.If(condition, trueBody, elseIfClauses, Optional.ofNullable(elseBody), position);
    }
    
    private Stmt.For forLoop() {
        SourcePosition position = consume("Expected 'for' keyword at beginning of for loop", TokenType.FOR_LOOP).position();
        consume("Expected '(' after 'for' keyword", TokenType.OPEN_PAREN);
        Stmt initializer = statement();
        Expr condition;
        if(current().isType(TokenType.STATEMENT_END)) {
            condition = new Expr.Literal(true, Type.BOOLEAN,
                                         current().position()); // If no condition is provided, set condition = true
            consumeUnchecked();
        } else {
            condition = expression();
            consumeStatementEnd("loop condition");
        }
        Expr incrementer;
        if(current().isType(TokenType.CLOSE_PAREN)) {
            incrementer = null;
            consumeUnchecked();
        } else {
            incrementer = expression();
            consume("Expected ')' after for loop incrementer", TokenType.CLOSE_PAREN);
        }
        Stmt.Block body = blockOrSingleStatement();
        return new Stmt.For(initializer, condition, incrementer, body, position);
    }
    
    private Stmt.While whileLoop() {
        SourcePosition position = consume("Expected 'for' keyword at beginning of while loop", TokenType.WHILE_LOOP).position();
        consume("Expected '(' after 'while' keyword", TokenType.OPEN_PAREN);
        Expr condition = expression();
        consume("Expected ')' after while loop condition", TokenType.CLOSE_PAREN);
        Stmt.Block body = blockOrSingleStatement();
        return new Stmt.While(condition, body, position);
    }
    
    private Stmt.Break breakStmt() {
        SourcePosition position = consume("Expected 'break' keyword for break statement", TokenType.BREAK).position();
        consumeStatementEnd("'break' keyword");
        return new Stmt.Break(position);
    }
    
    private Stmt.Continue continueStmt() {
        SourcePosition position = consume("Expected 'continue' keyword for continue statement", TokenType.CONTINUE).position();
        consumeStatementEnd("'continue' keyword");
        return new Stmt.Continue(position);
    }
    
    private Stmt.Block blockOrSingleStatement() {
        if(!current().isType(TokenType.BLOCK_BEGIN)) return new Stmt.Block(List.of(statement()), current().position());
        return block();
    }
    
    private Stmt.Block block() {
        SourcePosition position = consume("Expected '{' at start of block", TokenType.BLOCK_BEGIN).position();
        List<Stmt> statements = new ArrayList<>();
        while(!current().isType(TokenType.BLOCK_END)) {
            statements.add(statement());
        }
        consume("Expected '}' at end of block", TokenType.BLOCK_END);
        return new Stmt.Block(statements, position);
    }
    
    private Stmt expressionStatement() {
        Expr expression = expression();
        consumeStatementEnd("expression statement");
        return new Stmt.Expression(expression, expression.position);
    }
    
    private Expr expression() {
        return assignment();
    }
    
    private Expr leftAssociativeBinaryExpression(Supplier<Expr> higherPrecedence, BinaryOperator... operators) {
        Expr expr = higherPrecedence.get();
        loop:
        while(true) {
            for(BinaryOperator operator : operators) {
                if(current().isType(operator.tokenType)) {
                    SourcePosition position = consumeUnchecked().position(); // Consume operator token
                    expr = new Expr.Binary(expr, operator, higherPrecedence.get(), position);
                    continue loop;
                }
            }
            break; // Break if not any operator
        }
        return expr;
    }
    
    private Expr rightAssociativeBinaryExpression(Supplier<Expr> higherPrecedence, BinaryOperator... operators) {
        Expr expr = higherPrecedence.get();
        for(BinaryOperator operator : operators) {
            if(current().isType(operator.tokenType)) {
                SourcePosition position = consumeUnchecked().position(); // Consume operator token
                return new Expr.Binary(expr, operator, rightAssociativeBinaryExpression(higherPrecedence, operators), position);
            }
        }
        return expr;
    }
    
    private Expr assignment() {
        Expr expr = logicOr();
        if(current().isType(TokenType.ASSIGNMENT)) {
            SourcePosition position = consumeUnchecked().position(); // Consume operator token
            if(!(expr instanceof Variable variable)) throw new ParseException("Invalid assignment target", position);
            return new Expr.Assignment(variable, assignment(), position);
        }
        return expr;
    }
    
    private Expr logicOr() {
        return leftAssociativeBinaryExpression(this::logicAnd, BinaryOperator.BOOLEAN_OR);
    }
    
    private Expr logicAnd() {
        return leftAssociativeBinaryExpression(this::equality, BinaryOperator.BOOLEAN_AND);
    }
    
    private Expr equality() {
        return leftAssociativeBinaryExpression(this::comparison, BinaryOperator.EQUALS, BinaryOperator.NOT_EQUALS);
    }
    
    private Expr comparison() {
        return leftAssociativeBinaryExpression(this::term, BinaryOperator.GREATER, BinaryOperator.GREATER_EQUALS, BinaryOperator.LESS,
                                               BinaryOperator.LESS_EQUALS);
    }
    
    private Expr term() {
        return leftAssociativeBinaryExpression(this::factor, BinaryOperator.ADD, BinaryOperator.SUBTRACT);
    }
    
    private Expr factor() {
        return leftAssociativeBinaryExpression(this::unary, BinaryOperator.MULTIPLY, BinaryOperator.DIVIDE, BinaryOperator.MODULO);
    }
    
    private Expr unary() {
        UnaryOperator[] operators = { UnaryOperator.NOT, UnaryOperator.NEGATE };
        for(UnaryOperator operator : operators) {
            if(current().isType(operator.tokenType)) {
                SourcePosition position = consumeUnchecked().position();
                return new Expr.Unary(operator, unary(), position);
            }
        }
        return postfix();
    }
    
    private Expr postfix() {
        Expr expr = primary();
        while(current().isType(TokenType.OPEN_PAREN)) {
            expr = call(expr);
        }
        return expr;
    }
    
    private Expr primary() {
        Token token = consumeUnchecked();
        SourcePosition position = token.position();
        return switch(token.type()) {
            case NUMBER -> new Expr.Literal(Double.parseDouble(token.lexeme()), Type.NUMBER, position);
            case STRING -> new Expr.Literal(token.lexeme(), Type.STRING, position);
            case BOOLEAN -> new Expr.Literal(Boolean.parseBoolean(token.lexeme()), Type.BOOLEAN, position);
            case IDENTIFIER -> variable(token);
            case OPEN_PAREN -> {
                if(current().isType(TokenType.CLOSE_PAREN)) {
                    consumeUnchecked(); // Consume ')'
                    yield new Expr.Void(position); // () evaluates to void
                }
                Expr expr = expression();
                consume("Expected ')' to close '(' located at " + position, TokenType.CLOSE_PAREN);
                yield new Expr.Grouping(expr, position);
            }
            default -> throw new ParseException("Unexpected token '" + token.lexeme() + "'", position);
        };
    }
    
    private Expr call(Expr function) {
        SourcePosition position = consume("Expected '(' to initiate function call on function", TokenType.OPEN_PAREN).position();
        
        List<Expr> args = new ArrayList<>();
        while(!current().isType(TokenType.CLOSE_PAREN)) {
            args.add(expression());
            if(current().isType(TokenType.CLOSE_PAREN)) break;
            consume("Expected ',' or ')' after passed argument in function call", TokenType.SEPARATOR);
        }
        
        consume("Expected ')' after " + (args.size() == 0 ? "')'" : "arguments") + " in function call",
                TokenType.CLOSE_PAREN);
        
        return new Expr.Call(function, args, position);
    }
    
    private Expr variable(Token identifier) {
        return new Expr.Variable(identifier.lexeme(), identifier.position());
    }
    
    
}
