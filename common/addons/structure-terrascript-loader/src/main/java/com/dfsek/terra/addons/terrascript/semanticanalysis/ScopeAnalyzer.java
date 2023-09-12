package com.dfsek.terra.addons.terrascript.semanticanalysis;

import com.dfsek.terra.addons.terrascript.Environment;
import com.dfsek.terra.addons.terrascript.Environment.ScopeException.NonexistentSymbolException;
import com.dfsek.terra.addons.terrascript.Environment.ScopeException.SymbolTypeMismatchException;
import com.dfsek.terra.addons.terrascript.Environment.Symbol;
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
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.IdentifierAlreadyDeclaredException;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.UndefinedReferenceException;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class ScopeAnalyzer implements Visitor<Void>, Stmt.Visitor<Void> {
    
    private Environment currentScope;
    
    private final ErrorHandler errorHandler;
    
    
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
        expr.setEnvironment(currentScope);
        expr.arguments.forEach(e -> e.accept(this));
        return null;
    }
    
    @Override
    public Void visitVariableExpr(Variable expr) {
        String id = expr.identifier;
        try {
            expr.setSymbol(currentScope.getVariable(id));
        } catch(NonexistentSymbolException e) {
            errorHandler.add(
                    new UndefinedReferenceException("No variable by the name '" + id + "' is defined in this scope", expr.position));
        } catch(SymbolTypeMismatchException e) {
            errorHandler.add(new ParseException("Identifier '" + id + "' is not defined as a variable", expr.position));
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
                currentScope.put(param.getLeft(), new Environment.Symbol.Variable(param.getRight()));
            } catch(Environment.ScopeException.SymbolAlreadyExistsException e) {
                throw new IllegalStateException("Formal parameter '" + param.getLeft() + "' defined in '" + stmt.identifier +
                                                "' already exists in the function scope");
            }
        }
        stmt.body.accept(this);
        currentScope = currentScope.outer();
        try {
            Symbol.Function symbol = new Symbol.Function(stmt.returnType, stmt.parameters.stream().map(Pair::getRight).toList(), currentScope);
            stmt.setSymbol(symbol);
            currentScope.put(stmt.identifier, symbol);
        } catch(Environment.ScopeException.SymbolAlreadyExistsException e) {
            errorHandler.add(new IdentifierAlreadyDeclaredException("Name '" + stmt.identifier + "' is already defined in this scope",
                                                                    stmt.position));
        }
        return null;
    }
    
    @Override
    public Void visitVariableDeclarationStmt(Stmt.VariableDeclaration stmt) {
        stmt.value.accept(this);
        try {
            currentScope.put(stmt.identifier, new Environment.Symbol.Variable(stmt.type));
        } catch(Environment.ScopeException.SymbolAlreadyExistsException e) {
            errorHandler.add(new IdentifierAlreadyDeclaredException("Name '" + stmt.identifier + "' is already defined in this scope",
                                                                    stmt.position));
        }
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
