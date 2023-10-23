package com.dfsek.terra.addons.terrascript.v2.semanticanalysis;

import com.dfsek.terra.addons.terrascript.v2.Environment;
import com.dfsek.terra.addons.terrascript.v2.ErrorHandler;
import com.dfsek.terra.addons.terrascript.v2.ast.Stmt;
import com.dfsek.terra.addons.terrascript.v2.ast.TypedStmt;


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
