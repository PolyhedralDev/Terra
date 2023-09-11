package com.dfsek.terra.addons.terrascript.semanticanalysis;

import com.dfsek.terra.addons.terrascript.Environment;
import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.ast.Stmt;
import com.dfsek.terra.addons.terrascript.ast.TypedStmt;


public class SemanticAnalyzer {
    
    public static TypedStmt.Block analyze(Stmt.Block root, ErrorHandler errorHandler) throws Exception {
        new ScopeAnalyzer(Environment.global(), errorHandler).visitBlockStmt(root);
        errorHandler.throwAny();
        
        new FunctionReferenceAnalyzer(errorHandler).visitBlockStmt(root);
        errorHandler.throwAny();
        
        TypedStmt.Block checkedRoot = (TypedStmt.Block) new TypeChecker(errorHandler).visitBlockStmt(root);
        errorHandler.throwAny();
        
        return checkedRoot;
    }
    
}
