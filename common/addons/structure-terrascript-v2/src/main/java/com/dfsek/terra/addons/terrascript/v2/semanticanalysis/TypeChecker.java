package com.dfsek.terra.addons.terrascript.v2.semanticanalysis;

import java.util.List;
import java.util.Optional;

import com.dfsek.terra.addons.terrascript.v2.ErrorHandler;
import com.dfsek.terra.addons.terrascript.v2.Type;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Assignment;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Binary;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Call;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Grouping;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Literal;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Unary;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Variable;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Visitor;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Void;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedExpr;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.InvalidCalleeException;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.InvalidFunctionDeclarationException;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.InvalidTypeException;
import com.dfsek.terra.addons.terrascript.v2.parser.ParseException;
import com.dfsek.terra.api.util.generic.pair.Pair;

import static com.dfsek.terra.addons.terrascript.v2.util.OrdinalUtil.ordinalOf;


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
                if(!leftType.typeOf(Type.BOOLEAN) || !rightType.typeOf(Type.BOOLEAN))
                    errorHandler.add(new InvalidTypeException("Both operands of '" + expr.operator + "' operator must be of type '" + Type.BOOLEAN + "', found types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.BOOLEAN;
            }
            case EQUALS, NOT_EQUALS -> {
                if(!leftType.typeOf(rightType)) errorHandler.add(new InvalidTypeException("Both operands of equality operator (==) must be of the same type, found mismatched types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.BOOLEAN;
            }
            case GREATER, GREATER_EQUALS, LESS, LESS_EQUALS -> {
                if(!leftType.typeOf(Type.NUMBER) || !rightType.typeOf(Type.NUMBER))
                    errorHandler.add(new InvalidTypeException("Both operands of '" + expr.operator + "' operator must be of type '" + Type.NUMBER + "', found types '" + leftType + "' and '" + rightType + "'", expr.position));
                yield Type.BOOLEAN;
            }
            case ADD -> {
                if(leftType.typeOf(Type.NUMBER) && rightType.typeOf(Type.NUMBER)) {
                    yield Type.NUMBER;
                }
                if(leftType.typeOf(Type.STRING) || rightType.typeOf(Type.STRING)) {
                    yield Type.STRING;
                }
                errorHandler.add(new RuntimeException("Addition operands must be either both of type '" + Type.NUMBER + "', or one of type '" + Type.STRING + "'"));
                yield Type.VOID;
            }
            case SUBTRACT, MULTIPLY, DIVIDE, MODULO -> {
                if(!leftType.typeOf(Type.NUMBER) || !rightType.typeOf(Type.NUMBER))
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
                if(!right.type.typeOf(Type.BOOLEAN)) throw new RuntimeException();
                yield Type.BOOLEAN;
            }
            case NEGATE -> {
                if(!right.type.typeOf(Type.NUMBER)) throw new RuntimeException();
                yield Type.NUMBER;
            }
        };
        return new TypedExpr.Unary(expr.operator, right, type);
    }
    
    @Override
    public TypedExpr visitCallExpr(Call expr) {
        
        TypedExpr function = expr.callee.accept(this);
        
        if(!(function.type instanceof Type.Function functionType)) {
            errorHandler.add(new InvalidCalleeException("Cannot call type '" + function.type + "', only functions can be called", expr.position));
            return new TypedExpr.Void(Type.VOID);
        }
        
        List<TypedExpr> arguments = expr.arguments.stream().map(a -> a.accept(this)).toList();
        List<Type> parameters = functionType.getParameters();
        
        if(arguments.size() != parameters.size())
            errorHandler.add(new ParseException(
                    "Provided " + arguments.size() + " arguments to function call, expected " + parameters.size() + " arguments", expr.position));
        
        for(int i = 0; i < parameters.size(); i++) {
            Type expectedType = parameters.get(i);
            Type providedType = arguments.get(i).type;
            if(!expectedType.typeOf(providedType))
                errorHandler.add(new InvalidTypeException(
                        ordinalOf(i + 1) + " argument provided for function. Function expects type " + expectedType + ", found " +
                        providedType + " instead", expr.position));
        }
        
        return new TypedExpr.Call(function, arguments, functionType.getReturnType());
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
        if(!right.type.typeOf(expected))
            errorHandler.add(new InvalidTypeException(
                    "Cannot assign variable '" + id + "' to value of type '" + right.type + "', '" + id + "' is declared with type '" + expected + "'",
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
        TypedStmt.Block body = new TypedStmt.Block(stmt.body.statements.stream().map(s -> s.accept(this)).toList());
        boolean hasReturn = alwaysReturns(body, stmt);
        if(!stmt.returnType.typeOf(Type.VOID) && !hasReturn) {
            errorHandler.add(
                    new InvalidFunctionDeclarationException("Function body for '" + stmt.identifier + "' does not contain return statement",
                                                            stmt.position));
        }
        return new TypedStmt.FunctionDeclaration(stmt.identifier, stmt.parameters, stmt.returnType, body, ((Type.Function) stmt.getSymbol().type).getId());
    }
    
    private boolean alwaysReturns(TypedStmt stmt, Stmt.FunctionDeclaration function) {
        if(stmt instanceof TypedStmt.Return ret) {
            if(!ret.value.type.typeOf(function.returnType))
                errorHandler.add(new InvalidTypeException(
                        "Return statement must match function's return type. Function '" + function.identifier + "' expects " +
                        function.returnType + ", found " + ret.value.type + " instead", function.position));
            return true;
        } else if (stmt instanceof TypedStmt.If ifStmt) {
            return alwaysReturns(ifStmt.trueBody, function) &&
                   ifStmt.elseIfClauses.stream().map(Pair::getRight).allMatch(s -> alwaysReturns(s, function)) &&
                   ifStmt.elseBody.map(body -> alwaysReturns(body, function)).orElse(false); // If else body is not defined then statement does not always return
        } else if (stmt instanceof TypedStmt.Block block) {
            return block.statements.stream().anyMatch(s -> alwaysReturns(s, function));
        }
        return false;
    }
    
    @Override
    public TypedStmt visitVariableDeclarationStmt(Stmt.VariableDeclaration stmt) {
        TypedExpr value = stmt.value.accept(this);
        if(!stmt.type.typeOf(value.type))
            errorHandler.add(new InvalidTypeException(
                    "Type of value assigned to variable '" + stmt.identifier + "' does not match variable's declared type. Expected type '" +
                    stmt.type + "', found '" + value.type +"' instead", stmt.position));
        return new TypedStmt.VariableDeclaration(stmt.type, stmt.identifier, value);
    }
    
    @Override
    public TypedStmt visitReturnStmt(Stmt.Return stmt) {
        return new TypedStmt.Return(stmt.value.accept(this));
    }
    
    @Override
    public TypedStmt visitIfStmt(Stmt.If stmt) {
        TypedExpr condition = stmt.condition.accept(this);
        if(!condition.type.typeOf(Type.BOOLEAN)) errorHandler.add(new InvalidTypeException("If statement conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
        
        TypedStmt.Block trueBody = (TypedStmt.Block) stmt.trueBody.accept(this);
        List<Pair<TypedExpr, TypedStmt.Block>> elseIfClauses = stmt.elseIfClauses.stream().map(c -> {
            TypedExpr clauseCondition = c.getLeft().accept(this);
            if (!clauseCondition.type.typeOf(Type.BOOLEAN)) errorHandler.add(new InvalidTypeException("Else if clause conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
            return Pair.of(clauseCondition, (TypedStmt.Block) c.getRight().accept(this));
        }).toList();
        
        Optional<TypedStmt.Block> elseBody = stmt.elseBody.map(b -> (TypedStmt.Block) b.accept(this));
        
        return new TypedStmt.If(condition, trueBody, elseIfClauses, elseBody);
    }
    
    @Override
    public TypedStmt visitForStmt(Stmt.For stmt) {
        TypedStmt initializer = stmt.initializer.accept(this);
        TypedExpr condition = stmt.condition.accept(this);
        if(!condition.type.typeOf(Type.BOOLEAN)) errorHandler.add(new InvalidTypeException("For statement conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
        TypedExpr incrementer = stmt.incrementer.accept(this);
        return new TypedStmt.For(initializer, condition, incrementer, (TypedStmt.Block) stmt.body.accept(this));
    }
    
    @Override
    public TypedStmt visitWhileStmt(Stmt.While stmt) {
        TypedExpr condition = stmt.condition.accept(this);
        if(!condition.type.typeOf(Type.BOOLEAN)) errorHandler.add(new InvalidTypeException("While statement conditional must be of type '" + Type.BOOLEAN + "', found '" + condition.type + "' instead.", stmt.position));
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
