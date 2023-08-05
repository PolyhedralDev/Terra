package semanticanalysis;

import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.IdentifierAlreadyDeclaredException;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.UndefinedReferenceException;
import com.dfsek.terra.addons.terrascript.semanticanalysis.SemanticAnalyzer;
import com.dfsek.terra.addons.terrascript.lexer.Lexer;
import com.dfsek.terra.addons.terrascript.parser.Parser;

import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.InvalidFunctionDeclarationException;
import com.dfsek.terra.addons.terrascript.exception.semanticanalysis.InvalidTypeException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SemanticAnalyzerTest {
    
    @Test
    public void testVariableReference() {
        // Use of declared variable
        testValid("num a = 1; a + a;");
        
        // Can't use undeclared variable
        testInvalid("a + a;", UndefinedReferenceException.class);
        
        // Can't reference variable before declaration
        testInvalid("a + a; num a = 1;", UndefinedReferenceException.class);
        
        // Variable declarations shouldn't be accessible from inner scopes
        testInvalid("{ num a = 1; } a + a;", UndefinedReferenceException.class);
        
        // Can access variables declared in outer scope
        testValid("num a = 3; { a + a; }");
        
        // Should not be able to use variables from outer scope if they're declared after scope
        testInvalid("{ a + a; } num a = 2;", UndefinedReferenceException.class);
        
        // Can't use undeclared variable as function argument
        testInvalid("fun test(p: str) {} test(a);", UndefinedReferenceException.class);
        
        // Same as above, but in inner scope
        testInvalid("fun test(p: str) {} { test(a); }", UndefinedReferenceException.class);
        
        // Cannot assign undeclared variable
        testInvalid("a = 1;", UndefinedReferenceException.class);
        
        // Cannot assign to variable declared after assignment
        testInvalid("a = 2; num a = 1;", UndefinedReferenceException.class);
    }
    
    @Test
    public void testAssignment() {
        // Simple assignment
        testValid("num a = 1; a = 2;");
        
        // Can assign variables declared in outer scope
        testValid("""
                  num a = 1;
                  { a = 2; }
                  """);
        
        // Cannot assign variables declared in inner scope
        testInvalid("""
                    { num a = 1; }
                    a = 2;
                    """, UndefinedReferenceException.class);
        
        // Cannot assign variables declared in outer scope after reference
        testInvalid("""
                    { a = 2; }
                    num a = 1;
                    """, UndefinedReferenceException.class);
        
        // Cannot assign variable to expression of different type
        testInvalid("num a = 1; a = true;", InvalidTypeException.class);
    }
    
    @Test
    public void testReturnStatement() {
        // Function return must match signature
        testInvalid("fun returnBool(): bool {}", InvalidFunctionDeclarationException.class);
        testInvalid("fun returnNum(): num { return \"Not num\"; }", InvalidTypeException.class);
        
        // Return statements can be empty for void return
        testValid("fun returnVoid() { return; }");
        
        // Return statement returns type matching function signature
        testValid("fun returnNum(): num { return 3; }");
        
        testValid("fun returnVoid() { return (); }");
    }
    
    @Test
    public void testControlFlowAnalysis() {
        
        testValid("""
                  fun returnsNum(p: bool): num {
                      if (p) {
                          return 1;
                      } else {
                          return 0;
                      }
                  }
                  """);
        
        
        // All paths of execution must return a value if return type is not void
        testInvalid("""
                    fun returnsNum(p: bool): num {
                        if (p) {
                            return 1;
                        }
                    }
                    """, null);
        
        // Not all paths require explicit return for void functions, implicitly returns void at end of execution
        testValid("""
                    fun returnsNum(p: bool) {
                        if (p) {
                            return;
                        }
                    }
                    """);
        
        // Static analysis of if statement always being true
        testValid("""
                  fun returnsNum(): num {
                      if (true) {
                          return 1;
                      }
                  """);
    }
    
    @Test
    public void testFunctionCall() {
        // Simple function declaration then call
        testValid("fun test() {}; test();");
        
        // Can be used before declaration
        testValid("test(); fun test() {};");
        
        // Can be used from outer scope
        testValid("fun test() {}; { test(); }");
        
        // Can be used from outer scope before declaration
        testValid("{ test(); } fun test() {};");
        
        // Can be used in many outer scopes
        testValid("{{{{{ test(); }}}}} fun test() {};");
        
        // Calling function that hasn't been declared
        testInvalid("test();", UndefinedReferenceException.class);
        
        // Cannot use functions declared in inner scopes
        testInvalid("{ fun test() {} } test();", UndefinedReferenceException.class);
        testInvalid("test(); { fun test() {} }", UndefinedReferenceException.class);
        
        // Mutual recursion supported
        testValid("fun a() { b(); } fun b() { a(); }");
    }
    
    @Test
    public void testFunctionArgumentPassing() {
        // Simple argument passing
        testValid("fun test(p: num); test(1);");
        
        // Passing multiple arguments
        testValid("fun test(p1: num, p2: bool); test(1, false);");
        
        // Argument type must match parameter type
        testInvalid("fun test(p: num); test(false);", InvalidTypeException.class);
        
        // Function return
        testValid("""
                  fun returnBool(): bool {
                      return true;
                  }
                  fun takeBool(p: bool) {}
                  takeBool(returnBool());
                  """);
        
        // Should not be able to pass argument of type not matching parameter type
        testInvalid("""
                  fun returnBool(): bool {
                      return true;
                  }
                  fun takeNum(p: num) {}
                  takeNum(returnBool());
                  """, InvalidTypeException.class);
    }
    
    @Test
    public void testParameterUse() {
        // Function bodies should be able to use parameter names
        testValid("fun test(a: num, b: num) { a + b; }");
        testInvalid("fun test(a: num, b: num) { a + c; }", UndefinedReferenceException.class);
        
        // Function bodies can't use variables from outer scope
        testInvalid("num a = 1; fun doStuff() { a + 2; }", UndefinedReferenceException.class);
        testInvalid("fun doStuff() { a + 2; } num a = 1;", UndefinedReferenceException.class);
        
        // Type checking parameters
        testValid("fun takesNum(a: num) {} fun test(numberParam: num) { takesNum(numberParam); }");
        testInvalid("fun takesNum(a: num) {} fun test(boolParam: bool) { takesNum(boolParam); }", InvalidTypeException.class);
    }
    
    @Test
    public void testShadowing() {
        // Can't shadow variable in immediate scope
        testInvalid("num a = 1; num a = 2;", IdentifierAlreadyDeclaredException.class);
        
        // Can shadow variable from outer scope
        testValid("num a = 1; { num a = 2; }");
        
        // Can declare variable after same identifier is used previously in an inner scope
        testValid("{ num a = 2; } num a = 1;");
        
        // Ensure shadowed variable type is used
        testValid("""
                  fun takesNum(p: num) {}
                  bool a = false;
                  {
                      num a = 1;
                      takesNum(a);
                  }
                  """);
        
        // Should not be able to use type of shadowed variable in use of shadowing variable
        testInvalid("""
                  fun takesNum(p: num) {}
                  num a = false;
                  {
                      bool a = 1;
                      takesNum(a);
                  }
                  """, InvalidTypeException.class);
        
        // Functions can be shadowed in inner scopes
        testValid("""
                  fun test() {}
                  {
                    fun test() {}
                  }
                  {
                    fun test() {}
                  }
                  """);
        
        // Functions can't be shadowed in the same immediate scope
        testInvalid("""
                  fun test() {}
                  fun test() {}
                  """, IdentifierAlreadyDeclaredException.class);
        
        // Can't use function name that is already declared as a variable
        testInvalid("num id = 1; fun id() {}", IdentifierAlreadyDeclaredException.class);
    }
    
    private <T extends Exception> void testInvalid(String invalidSource, Class<T> exceptionType) {
        ErrorHandler errorHandler = new ErrorHandler();
        assertThrows(exceptionType, () -> SemanticAnalyzer.analyze(Parser.parse(new Lexer(invalidSource).analyze()), errorHandler));
    }
    
    private void testValid(String validSource) {
        ErrorHandler errorHandler = new ErrorHandler();
        assertDoesNotThrow(() -> SemanticAnalyzer.analyze(Parser.parse(new Lexer(validSource).analyze()), errorHandler));
    }
}
