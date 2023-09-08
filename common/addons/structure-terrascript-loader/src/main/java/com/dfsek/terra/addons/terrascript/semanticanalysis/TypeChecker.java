package com.dfsek.terra.addons.terrascript.semanticanalysis;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Environment;
import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.ast.Expr;
import com.dfsek.terra.addons.terrascript.ast.Expr.Assignment;
import com.dfsek.terra.addons.terrascript.ast.Expr.Binary;
import com.dfsek.terra.addons.terrascript.ast.Expr.Call;
import com.dfsek.terra.addons.terrascript.ast.Expr.Grouping;
import com.dfsek.terra.addons.terrascript.ast.Expr.Literal;
import com.dfsek.terra.addons.terrascript.ast.Expr.Unary;
import com.dfsek.terra.addons.terrascript.ast.Expr.Variable;
import com.dfsek.terra.addons.terrascript.ast.Expr.Visitor;
import com.dfsek.terra.addons.terrascript.ast.Expr.Void;
import com.dfsek.terra.addons.terrascript.ast.Stmt;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.InvalidFunctionDeclarationException;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.InvalidTypeException;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.api.util.generic.pair.Pair;

import static com.dfsek.terra.addons.terrascript.util.OrdinalUtil.ordinalOf;


public class TypeChecker implements Visitor<Type>, Stmt.Visitor<Type> {
    
    private final ErrorHandler errorHandler;
    
    TypeChecker(ErrorHandler errorHandler) { this.errorHandler = errorHandler; }
    
    @Override
    public Type visitBinaryExpr(Binary expr) {
        Type left = expr.left.accept(this);
        Type right = expr.right.accept(this);
        
        return switch(expr.operator) {
            case BOOLEAN_OR, BOOLEAN_AND -> {
                if(left != Type.BOOLEAN || right != Type.BOOLEAN)
                    throw new RuntimeException();
                yield Type.BOOLEAN;
            }
            case EQUALS, NOT_EQUALS -> {
                if(left != right) throw new RuntimeException();
                yield Type.BOOLEAN;
            }
            case GREATER, GREATER_EQUALS, LESS, LESS_EQUALS -> {
                if(left != Type.NUMBER || right != Type.NUMBER)
                    throw new RuntimeException();
                yield Type.BOOLEAN;
            }
            case ADD -> {
                if(left == Type.NUMBER && right == Type.NUMBER) {
                    expr.setType(Type.NUMBER);
                    yield Type.NUMBER;
                }
                if(left == Type.STRING || right == Type.STRING) {
                    expr.setType(Type.STRING);
                    yield Type.STRING;
                }
                throw new RuntimeException("Addition operands must be either both numbers, or one of type string");
            }
            case SUBTRACT, MULTIPLY, DIVIDE, MODULO -> {
                if(left != Type.NUMBER || right != Type.NUMBER)
                    throw new RuntimeException();
                yield Type.NUMBER;
            }
        };
    }
    
    @Override
    public Type visitGroupingExpr(Grouping expr) {
        return expr.expression.accept(this);
    }
    
    @Override
    public Type visitLiteralExpr(Literal expr) {
        return expr.type;
    }
    
    @Override
    public Type visitUnaryExpr(Unary expr) {
        Type right = expr.operand.accept(this);
        return switch(expr.operator) {
            case NOT -> {
                if(right != Type.BOOLEAN) throw new RuntimeException();
                yield Type.BOOLEAN;
            }
            case NEGATE -> {
                if(right != Type.NUMBER) throw new RuntimeException();
                yield Type.NUMBER;
            }
        };
    }
    
    @Override
    public Type visitCallExpr(Call expr) {
        String id = expr.identifier;
        
        Environment.Symbol.Function signature = expr.getSymbol();
        
        List<Type> argumentTypes = expr.arguments.stream().map(a -> a.accept(this)).toList();
        List<Type> parameters = signature.parameters.stream().map(Pair::getRight).toList();
        
        if(argumentTypes.size() != parameters.size())
            errorHandler.add(new ParseException(
                    "Provided " + argumentTypes.size() + " arguments to function call of '" + id + "', expected " + parameters.size() +
                    " arguments", expr.position));
        
        for(int i = 0; i < parameters.size(); i++) {
            Type expectedType = parameters.get(i);
            Type providedType = argumentTypes.get(i);
            if(expectedType != providedType)
                errorHandler.add(new InvalidTypeException(
                        ordinalOf(i + 1) + " argument provided for function '" + id + "' expects type " + expectedType + ", found " +
                        providedType + " instead", expr.position));
        }
        
        return signature.type;
    }
    
    @Override
    public Type visitVariableExpr(Variable expr) {
        return expr.getSymbol().type;
    }
    
    @Override
    public Type visitAssignmentExpr(Assignment expr) {
        Type right = expr.rValue.accept(this);
        Type expected = expr.lValue.accept(this);
        String id = expr.lValue.identifier;
        if(right != expected)
            errorHandler.add(new InvalidTypeException(
                    "Cannot assign variable '" + id + "' to type " + right + ", '" + id + "' is declared with type " + expected,
                    expr.position));
        return right;
    }
    
    @Override
    public Type visitVoidExpr(Void expr) {
        return Type.VOID;
    }
    
    @Override
    public Type visitExpressionStmt(Stmt.Expression stmt) {
        stmt.expression.accept(this);
        return Type.VOID;
    }
    
    @Override
    public Type visitBlockStmt(Stmt.Block stmt) {
        stmt.statements.forEach(s -> s.accept(this));
        return Type.VOID;
    }
    
    @Override
    public Type visitFunctionDeclarationStmt(Stmt.FunctionDeclaration stmt) {
        boolean hasReturn = false;
        for(Stmt s : stmt.body.statements) {
            if(s instanceof Stmt.Return ret) {
                hasReturn = true;
                Type provided = ret.value.accept(this);
                if(provided != stmt.type)
                    errorHandler.add(new InvalidTypeException(
                            "Return statement must match function's return type. Function '" + stmt.identifier + "' expects " +
                            stmt.type + ", found " + provided + " instead", s.position));
            }
            s.accept(this);
        }
        if(stmt.type != Type.VOID && !hasReturn) {
            errorHandler.add(
                    new InvalidFunctionDeclarationException("Function body for '" + stmt.identifier + "' does not contain return statement",
                                                            stmt.position));
        }
        return Type.VOID;
    }
    
    @Override
    public Type visitVariableDeclarationStmt(Stmt.VariableDeclaration stmt) {
        Type valueType = stmt.value.accept(this);
        if(stmt.type != valueType)
            errorHandler.add(new InvalidTypeException(
                    "Type " + stmt.type + " declared for variable '" + stmt.identifier + "' does not match assigned value type " +
                    valueType, stmt.position));
        return Type.VOID;
    }
    
    @Override
    public Type visitReturnStmt(Stmt.Return stmt) {
        stmt.setType(stmt.value.accept(this));
        return Type.VOID;
    }
    
    @Override
    public Type visitIfStmt(Stmt.If stmt) {
        if(stmt.condition.accept(this) != Type.BOOLEAN) throw new RuntimeException();
        stmt.trueBody.accept(this);
        for(Pair<Expr, Stmt.Block> clause : stmt.elseIfClauses) {
            if(clause.getLeft().accept(this) != Type.BOOLEAN) throw new RuntimeException();
            clause.getRight().accept(this);
        }
        if(stmt.elseBody != null) {
            stmt.elseBody.accept(this);
        }
        return Type.VOID;
    }
    
    @Override
    public Type visitForStmt(Stmt.For stmt) {
        stmt.initializer.accept(this);
        if(stmt.condition.accept(this) != Type.BOOLEAN) throw new RuntimeException();
        stmt.incrementer.accept(this);
        stmt.body.accept(this);
        return Type.VOID;
    }
    
    @Override
    public Type visitWhileStmt(Stmt.While stmt) {
        if(stmt.condition.accept(this) != Type.BOOLEAN) throw new RuntimeException();
        stmt.body.accept(this);
        return Type.VOID;
    }
    
    @Override
    public Type visitNoOpStmt(Stmt.NoOp stmt) {
        return Type.VOID;
    }
    
    @Override
    public Type visitBreakStmt(Stmt.Break stmt) {
        return Type.VOID;
    }
    
    @Override
    public Type visitContinueStmt(Stmt.Continue stmt) {
        return Type.VOID;
    }
    
}
