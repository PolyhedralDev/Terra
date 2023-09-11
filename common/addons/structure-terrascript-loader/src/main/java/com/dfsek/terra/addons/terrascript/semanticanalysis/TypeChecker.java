package com.dfsek.terra.addons.terrascript.semanticanalysis;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dfsek.terra.addons.terrascript.Environment;
import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.Type;
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
import com.dfsek.terra.addons.terrascript.ast.TypedExpr;
import com.dfsek.terra.addons.terrascript.ast.TypedStmt;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.InvalidFunctionDeclarationException;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.InvalidTypeException;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.api.util.generic.pair.Pair;

import static com.dfsek.terra.addons.terrascript.util.OrdinalUtil.ordinalOf;


public class TypeChecker implements Visitor<TypedExpr>, Stmt.Visitor<TypedStmt> {
    
    private final ErrorHandler errorHandler;
    
    TypeChecker(ErrorHandler errorHandler) { this.errorHandler = errorHandler; }
    
    @Override
    public TypedExpr visitBinaryExpr(Binary expr) {
        TypedExpr left = expr.left.accept(this);
        TypedExpr right = expr.right.accept(this);
        
        Type leftType = left.type;
        Type rightType = right.type;
        
        Type type = switch(expr.operator) {
            case BOOLEAN_OR, BOOLEAN_AND -> {
                if(leftType != Type.BOOLEAN || rightType != Type.BOOLEAN)
                    errorHandler.add(new InvalidTypeException("Both operands of '" + expr.operator + "' operator must be of type '" + Type.NUMBER + "', found types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.BOOLEAN;
            }
            case EQUALS, NOT_EQUALS -> {
                if(leftType != rightType) errorHandler.add(new InvalidTypeException("Both operands of equality operator (==) must be of the same type, found mismatched types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.BOOLEAN;
            }
            case GREATER, GREATER_EQUALS, LESS, LESS_EQUALS -> {
                if(leftType != Type.NUMBER || rightType != Type.NUMBER)
                    errorHandler.add(new InvalidTypeException("Both operands of '" + expr.operator + "' operator must be of type '" + Type.NUMBER + "', found types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.BOOLEAN;
            }
            case ADD -> {
                if(leftType == Type.NUMBER && rightType == Type.NUMBER) {
                    yield Type.NUMBER;
                }
                if(leftType == Type.STRING || rightType == Type.STRING) {
                    yield Type.STRING;
                }
                errorHandler.add(new RuntimeException("Addition operands must be either both numbers, or one of type string"));
                yield Type.VOID;
            }
            case SUBTRACT, MULTIPLY, DIVIDE, MODULO -> {
                if(leftType != Type.NUMBER || rightType != Type.NUMBER)
                    errorHandler.add(new InvalidTypeException("Both operands of '" + expr.operator + "' operator must be of type '" + Type.NUMBER + "', found types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.NUMBER;
            }
        };
        return new TypedExpr.Binary(left, expr.operator, right, type);
    }
    
    @Override
    public TypedExpr visitGroupingExpr(Grouping expr) {
        return expr.expression.accept(this);
    }
    
    @Override
    public TypedExpr visitLiteralExpr(Literal expr) {
        return new TypedExpr.Literal(expr.value, expr.type);
    }
    
    @Override
    public TypedExpr visitUnaryExpr(Unary expr) {
        TypedExpr right = expr.operand.accept(this);
        Type type = switch(expr.operator) {
            case NOT -> {
                if(right.type != Type.BOOLEAN) throw new RuntimeException();
                yield Type.BOOLEAN;
            }
            case NEGATE -> {
                if(right.type != Type.NUMBER) throw new RuntimeException();
                yield Type.NUMBER;
            }
        };
        return new TypedExpr.Unary(expr.operator, right, type);
    }
    
    @Override
    public TypedExpr visitCallExpr(Call expr) {
        String id = expr.identifier;
        
        Environment.Symbol.Function signature = expr.getSymbol();
        
        List<TypedExpr> arguments = expr.arguments.stream().map(a -> a.accept(this)).toList();
        List<Type> parameters = signature.parameters.stream().map(Pair::getRight).toList();
        
        if(arguments.size() != parameters.size())
            errorHandler.add(new ParseException(
                    "Provided " + arguments.size() + " arguments to function call of '" + id + "', expected " + parameters.size() +
                    " arguments", expr.position));
        
        for(int i = 0; i < parameters.size(); i++) {
            Type expectedType = parameters.get(i);
            Type providedType = arguments.get(i).type;
            if(expectedType != providedType)
                errorHandler.add(new InvalidTypeException(
                        ordinalOf(i + 1) + " argument provided for function '" + id + "' expects type " + expectedType + ", found " +
                        providedType + " instead", expr.position));
        }
        
        return new TypedExpr.Call(expr.identifier, arguments, signature.type);
    }
    
    @Override
    public TypedExpr visitVariableExpr(Variable expr) {
        return new TypedExpr.Variable(expr.identifier, expr.getSymbol().type);
    }
    
    @Override
    public TypedExpr visitAssignmentExpr(Assignment expr) {
        TypedExpr.Variable left = (TypedExpr.Variable) expr.lValue.accept(this);
        TypedExpr right = expr.rValue.accept(this);
        Type expected = left.type;
        String id = expr.lValue.identifier;
        if(right.type != expected)
            errorHandler.add(new InvalidTypeException(
                    "Cannot assign variable '" + id + "' to type " + right + ", '" + id + "' is declared with type " + expected,
                    expr.position));
        return new TypedExpr.Assignment(left, right, right.type);
    }
    
    @Override
    public TypedExpr visitVoidExpr(Void expr) {
        return new TypedExpr.Void(Type.VOID);
    }
    
    @Override
    public TypedStmt visitExpressionStmt(Stmt.Expression stmt) {
        return new TypedStmt.Expression(stmt.expression.accept(this));
    }
    
    @Override
    public TypedStmt visitBlockStmt(Stmt.Block stmt) {
        return new TypedStmt.Block(stmt.statements.stream().map(s -> s.accept(this)).toList());
    }
    
    @Override
    public TypedStmt visitFunctionDeclarationStmt(Stmt.FunctionDeclaration stmt) {
        AtomicBoolean hasReturn = new AtomicBoolean(false);
        TypedStmt.Block body = new TypedStmt.Block(stmt.body.statements.stream().map(s -> {
            TypedStmt bodyStmt = s.accept(this);
            if(bodyStmt instanceof TypedStmt.Return ret) {
                hasReturn.set(true);
                if(ret.value.type != stmt.returnType)
                    errorHandler.add(new InvalidTypeException(
                            "Return statement must match function's return type. Function '" + stmt.identifier + "' expects " +
                            stmt.returnType + ", found " + ret.value.type + " instead", s.position));
            }
            return bodyStmt;
        }).toList());
        if(stmt.returnType != Type.VOID && !hasReturn.get()) {
            errorHandler.add(
                    new InvalidFunctionDeclarationException("Function body for '" + stmt.identifier + "' does not contain return statement",
                                                            stmt.position));
        }
        return new TypedStmt.FunctionDeclaration(stmt.identifier, stmt.parameters, stmt.returnType, body);
    }
    
    @Override
    public TypedStmt visitVariableDeclarationStmt(Stmt.VariableDeclaration stmt) {
        TypedExpr value = stmt.value.accept(this);
        if(stmt.type != value.type)
            errorHandler.add(new InvalidTypeException(
                    "Type " + stmt.type + " declared for variable '" + stmt.identifier + "' does not match assigned value type " +
                    value.type, stmt.position));
        return new TypedStmt.VariableDeclaration(stmt.type, stmt.identifier, value);
    }
    
    @Override
    public TypedStmt visitReturnStmt(Stmt.Return stmt) {
        return new TypedStmt.Return(stmt.value.accept(this));
    }
    
    @Override
    public TypedStmt visitIfStmt(Stmt.If stmt) {
        TypedExpr condition = stmt.condition.accept(this);
        if(condition.type != Type.BOOLEAN) errorHandler.add(new InvalidTypeException("If statement conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
        
        TypedStmt.Block trueBody = (TypedStmt.Block) stmt.trueBody.accept(this);
        List<Pair<TypedExpr, TypedStmt.Block>> elseIfClauses = stmt.elseIfClauses.stream().map(c -> {
            TypedExpr clauseCondition = c.getLeft().accept(this);
            if (clauseCondition.type != Type.BOOLEAN) errorHandler.add(new InvalidTypeException("Else if clause conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
            return Pair.of(clauseCondition, (TypedStmt.Block) c.getRight().accept(this));
        }).toList();
        
        Optional<TypedStmt.Block> elseBody = stmt.elseBody.map(b -> (TypedStmt.Block) b.accept(this));
        
        return new TypedStmt.If(condition, trueBody, elseIfClauses, elseBody);
    }
    
    @Override
    public TypedStmt visitForStmt(Stmt.For stmt) {
        TypedStmt initializer = stmt.initializer.accept(this);
        TypedExpr condition = stmt.condition.accept(this);
        if(condition.type != Type.BOOLEAN) errorHandler.add(new InvalidTypeException("For statement conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
        TypedExpr incrementer = stmt.incrementer.accept(this);
        return new TypedStmt.For(initializer, condition, incrementer, (TypedStmt.Block) stmt.body.accept(this));
    }
    
    @Override
    public TypedStmt visitWhileStmt(Stmt.While stmt) {
        TypedExpr condition = stmt.condition.accept(this);
        if(condition.type != Type.BOOLEAN) errorHandler.add(new InvalidTypeException("While statement conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
        return new TypedStmt.While(condition, (TypedStmt.Block) stmt.body.accept(this));
    }
    
    @Override
    public TypedStmt visitNoOpStmt(Stmt.NoOp stmt) {
        return new TypedStmt.NoOp();
    }
    
    @Override
    public TypedStmt visitBreakStmt(Stmt.Break stmt) {
        return new TypedStmt.Break();
    }
    
    @Override
    public TypedStmt visitContinueStmt(Stmt.Continue stmt) {
        return new TypedStmt.Continue();
    }
}
