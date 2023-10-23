package com.dfsek.terra.addons.terrascript.semanticanalysis;

import com.dfsek.terra.addons.terrascript.Environment.ScopeException.NonexistentSymbolException;
import com.dfsek.terra.addons.terrascript.Environment.ScopeException.SymbolTypeMismatchException;
import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.ast.Expr;
import com.dfsek.terra.addons.terrascript.ast.Expr.Assignment;
import com.dfsek.terra.addons.terrascript.ast.Expr.Binary;
import com.dfsek.terra.addons.terrascript.ast.Expr.Call;
import com.dfsek.terra.addons.terrascript.ast.Expr.Grouping;
import com.dfsek.terra.addons.terrascript.ast.Expr.Literal;
import com.dfsek.terra.addons.terrascript.ast.Expr.Unary;
import com.dfsek.terra.addons.terrascript.ast.Expr.Variable;
import com.dfsek.terra.addons.terrascript.ast.Expr.Void;
import com.dfsek.terra.addons.terrascript.ast.Stmt;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Block;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Break;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Continue;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Expression;
import com.dfsek.terra.addons.terrascript.ast.Stmt.For;
import com.dfsek.terra.addons.terrascript.ast.Stmt.FunctionDeclaration;
import com.dfsek.terra.addons.terrascript.ast.Stmt.If;
import com.dfsek.terra.addons.terrascript.ast.Stmt.NoOp;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Return;
import com.dfsek.terra.addons.terrascript.ast.Stmt.VariableDeclaration;
import com.dfsek.terra.addons.terrascript.ast.Stmt.While;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.UndefinedReferenceException;
import com.dfsek.terra.addons.terrascript.parser.ParseException;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class FunctionReferenceAnalyzer implements Expr.Visitor<Void>, Stmt.Visitor<Void> {
    
    private final ErrorHandler errorHandler;
    
    public FunctionReferenceAnalyzer(ErrorHandler errorHandler) { this.errorHandler = errorHandler; }
    
    @Override
    public Void visitBinaryExpr(Binary expr) {
        expr.left.accept(this);
        expr.right.accept(this);
        return null;
    }
    
    @Override
    public Void visitGroupingExpr(Grouping expr) {
        expr.expression.accept(this);
        return null;
    }
    
    @Override
    public Void visitLiteralExpr(Literal expr) {
        return null;
    }
    
    @Override
    public Void visitUnaryExpr(Unary expr) {
        expr.operand.accept(this);
        return null;
    }
    
    @Override
    public Void visitCallExpr(Call expr) {
        String id = expr.identifier;
        try {
            expr.setSymbol(expr.getEnvironment().getFunction(expr.identifier));
        } catch(NonexistentSymbolException e) {
            errorHandler.add(
                    new UndefinedReferenceException("No function by the name '" + id + "' is defined in this scope", expr.position));
        } catch(SymbolTypeMismatchException e) {
            errorHandler.add(new ParseException("Identifier '" + id + "' is not defined as a function", expr.position));
        }
        expr.arguments.forEach(e -> e.accept(this));
        return null;
    }
    
    @Override
    public Void visitVariableExpr(Variable expr) {
        return null;
    }
    
    @Override
    public Void visitAssignmentExpr(Assignment expr) {
        expr.lValue.accept(this);
        expr.rValue.accept(this);
        return null;
    }
    
    @Override
    public Void visitVoidExpr(Void expr) {
        return null;
    }
    
    @Override
    public Void visitExpressionStmt(Expression stmt) {
        stmt.expression.accept(this);
        return null;
    }
    
    @Override
    public Void visitBlockStmt(Block stmt) {
        stmt.statements.forEach(s -> s.accept(this));
        return null;
    }
    
    @Override
    public Void visitFunctionDeclarationStmt(FunctionDeclaration stmt) {
        stmt.body.accept(this);
        return null;
    }
    
    @Override
    public Void visitVariableDeclarationStmt(VariableDeclaration stmt) {
        stmt.value.accept(this);
        return null;
    }
    
    @Override
    public Void visitReturnStmt(Return stmt) {
        stmt.value.accept(this);
        return null;
    }
    
    @Override
    public Void visitIfStmt(If stmt) {
        stmt.condition.accept(this);
        stmt.trueBody.accept(this);
        for(Pair<Expr, Block> clause : stmt.elseIfClauses) {
            clause.getLeft().accept(this);
            clause.getRight().accept(this);
        }
        stmt.elseBody.ifPresent(b -> b.accept(this));
        return null;
    }
    
    @Override
    public Void visitForStmt(For stmt) {
        stmt.initializer.accept(this);
        stmt.condition.accept(this);
        stmt.incrementer.accept(this);
        stmt.body.accept(this);
        return null;
    }
    
    @Override
    public Void visitWhileStmt(While stmt) {
        stmt.condition.accept(this);
        stmt.body.accept(this);
        return null;
    }
    
    @Override
    public Void visitNoOpStmt(NoOp stmt) {
        return null;
    }
    
    @Override
    public Void visitBreakStmt(Break stmt) {
        return null;
    }
    
    @Override
    public Void visitContinueStmt(Continue stmt) {
        return null;
    }
}
