package com.dfsek.terra.addons.terrascript.v2.semanticanalysis;

import java.util.List;

import com.dfsek.terra.addons.terrascript.v2.Environment;
import com.dfsek.terra.addons.terrascript.v2.Environment.Symbol;
import com.dfsek.terra.addons.terrascript.v2.ErrorHandler;
import com.dfsek.terra.addons.terrascript.v2.Type;
import com.dfsek.terra.addons.terrascript.v2.Type.Function;
import com.dfsek.terra.addons.terrascript.v2.ast.Expr;
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
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.IdentifierAlreadyDeclaredException;
import com.dfsek.terra.api.util.generic.pair.Pair;

import static com.dfsek.terra.addons.terrascript.v2.Environment.ScopeException.SymbolAlreadyExistsException;


public class ScopeAnalyzer implements Visitor<Void>, Stmt.Visitor<Void> {
    
    private final ErrorHandler errorHandler;
    private Environment currentScope;
    
    
    public ScopeAnalyzer(Environment globalScope, ErrorHandler errorHandler) {
        this.currentScope = globalScope;
        this.errorHandler = errorHandler;
    }
    
    @Override
    public Void visitBinaryExpr(Binary expr) {
        expr.right.accept(this);
        expr.left.accept(this);
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
        expr.setScope(currentScope);
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
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        stmt.expression.accept(this);
        return null;
    }
    
    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        currentScope = currentScope.lexicalInner();
        stmt.statements.forEach(s -> s.accept(this));
        currentScope = currentScope.outer();
        return null;
    }
    
    @Override
    public Void visitFunctionDeclarationStmt(Stmt.FunctionDeclaration stmt) {
        currentScope = currentScope.functionalInner();
        for(Pair<String, Type> param : stmt.parameters) {
            try {
                currentScope.put(param.getLeft(), new Symbol.Variable(param.getRight()));
            } catch(SymbolAlreadyExistsException e) {
                throw new IllegalStateException("Formal parameter '" + param.getLeft() + "' defined in '" + stmt.identifier +
                                                "' already exists in the function scope");
            }
        }
        stmt.body.accept(this);
        currentScope = currentScope.outer();
        try {
            List<Type> parameters = stmt.parameters.stream().map(Pair::getRight).toList();
            Function function = new Function(stmt.returnType, parameters, stmt.identifier, currentScope);
            Symbol.Variable symbol = new Symbol.Variable(function);
            stmt.setSymbol(symbol);
            currentScope.put(stmt.identifier, symbol);
        } catch(SymbolAlreadyExistsException e) {
            errorHandler.add(new IdentifierAlreadyDeclaredException("Name '" + stmt.identifier + "' is already defined in this scope",
                                                                    stmt.position));
        }
        return null;
    }
    
    @Override
    public Void visitVariableDeclarationStmt(Stmt.VariableDeclaration stmt) {
        stmt.setScope(currentScope);
        stmt.value.accept(this);
        return null;
    }
    
    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        stmt.value.accept(this);
        return null;
    }
    
    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        stmt.condition.accept(this);
        stmt.trueBody.accept(this);
        for(Pair<Expr, Stmt.Block> clause : stmt.elseIfClauses) {
            clause.getLeft().accept(this);
            clause.getRight().accept(this);
        }
        stmt.elseBody.ifPresent(b -> b.accept(this));
        return null;
    }
    
    @Override
    public Expr.Void visitForStmt(Stmt.For stmt) {
        currentScope = currentScope.loopInner(); // Loop initializer, condition, and incrementer belong to inner scope
        
        stmt.initializer.accept(this);
        stmt.condition.accept(this);
        stmt.incrementer.accept(this);
        stmt.body.accept(this);
        
        currentScope = currentScope.outer();
        return null;
    }
    
    @Override
    public Expr.Void visitWhileStmt(Stmt.While stmt) {
        stmt.condition.accept(this);
        currentScope = currentScope.loopInner();
        stmt.body.accept(this);
        currentScope = currentScope.outer();
        return null;
    }
    
    @Override
    public Expr.Void visitNoOpStmt(Stmt.NoOp stmt) {
        return null;
    }
    
    @Override
    public Expr.Void visitBreakStmt(Stmt.Break stmt) {
        return null;
    }
    
    @Override
    public Expr.Void visitContinueStmt(Stmt.Continue stmt) {
        return null;
    }
    
}
