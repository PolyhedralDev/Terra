package com.dfsek.terra.addons.terrascript.v2.semanticanalysis;

import com.dfsek.terra.addons.terrascript.v2.Environment.ScopeException.NonexistentSymbolException;
import com.dfsek.terra.addons.terrascript.v2.Environment.ScopeException.SymbolAlreadyExistsException;
import com.dfsek.terra.addons.terrascript.v2.Environment.Symbol;
import com.dfsek.terra.addons.terrascript.v2.ErrorHandler;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Assignment;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Binary;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Call;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Grouping;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Literal;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Unary;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Variable;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr.Void;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.Block;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.Break;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.Continue;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.Expression;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.For;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.FunctionDeclaration;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.If;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.NoOp;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.Return;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.VariableDeclaration;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt.While;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.IdentifierAlreadyDeclaredException;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.UndefinedReferenceException;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class VariableAnalyzer implements Expr.Visitor<Void>, Stmt.Visitor<Void> {
    
    private final ErrorHandler errorHandler;
    
    public VariableAnalyzer(ErrorHandler errorHandler) { this.errorHandler = errorHandler; }
    
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
        expr.callee.accept(this);
        expr.arguments.forEach(e -> e.accept(this));
        return null;
    }
    
    @Override
    public Void visitVariableExpr(Variable expr) {
        String id = expr.identifier;
        try {
            expr.setSymbol(expr.getScope().getVariable(id));
        } catch(NonexistentSymbolException e) {
            errorHandler.add(
                    new UndefinedReferenceException("'" + id + "' not is defined in this scope", expr.position));
        }
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
        try {
            stmt.getScope().put(stmt.identifier, new Symbol.Variable(stmt.type));
        } catch(SymbolAlreadyExistsException e) {
            errorHandler.add(new IdentifierAlreadyDeclaredException("Name '" + stmt.identifier + "' is already defined in this scope",
                                                                    stmt.position));
        }
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
