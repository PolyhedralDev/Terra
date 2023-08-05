package com.dfsek.terra.addons.terrascript.semanticanalysis;

import com.dfsek.terra.addons.terrascript.Environment;
import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.ast.Stmt;


public class SemanticAnalyzer {
    
    public static void analyze(Stmt.Block root, ErrorHandler errorHandler) throws Exception {
        new ScopeAnalyzer(Environment.global(), errorHandler).visitBlockStmt(root);
        errorHandler.throwAny();
        
        new FunctionReferenceAnalyzer(errorHandler).visitBlockStmt(root);
        errorHandler.throwAny();
        
        new TypeChecker(errorHandler).visitBlockStmt(root);
        errorHandler.throwAny();
    }
    
}
