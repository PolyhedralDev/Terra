package semanticanalysis;

import com.dfsek.terra.addons.terrascript.v2.ErrorHandler;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.IdentifierAlreadyDeclaredException;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.InvalidCalleeException;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.UndefinedReferenceException;
import com.dfsek.terra.addons.terrascript.v2.semanticanalysis.SemanticAnalyzer;
import com.dfsek.terra.addons.terrascript.v2.lexer.Lexer;
import com.dfsek.terra.addons.terrascript.v2.parser.Parser;

import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.InvalidFunctionDeclarationException;
import com.dfsek.terra.addons.terrascript.v2.exception.semanticanalysis.InvalidTypeException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SemanticAnalyzerTest {
    
    @Test
    public void testVariableReference() {
        // Use of declared variable
        testValid("var a: num = 1; a + a;");
        
        // Can't use undeclared variable
        testInvalid("a + a;", UndefinedReferenceException.class);
        
        // Can't reference variable before declaration
        testInvalid("a + a; var a: num = 1;", UndefinedReferenceException.class);
        
        // Variable declarations shouldn't be accessible from inner scopes
        testInvalid("{ var a: num = 1; } a + a;", UndefinedReferenceException.class);
        
        // Can access variables declared in outer scope
        testValid("var a: num = 3; { a + a; }");
        
        // Should not be able to use variables from outer scope if they're declared after scope
        testInvalid("{ a + a; } var a: num = 2;", UndefinedReferenceException.class);
        
        // Can't use undeclared variable as function argument
        testInvalid("fun test(p: str) {} test(a);", UndefinedReferenceException.class);
        
        // Same as above, but in inner scope
        testInvalid("fun test(p: str) {} { test(a); }", UndefinedReferenceException.class);
        
        // Cannot assign undeclared variable
        testInvalid("a = 1;", UndefinedReferenceException.class);
        
        // Cannot assign to variable declared after assignment
        testInvalid("a = 2; var a: num = 1;", UndefinedReferenceException.class);
    }
    
    @Test
    public void testAssignment() {
        // Simple assignment
        testValid("var a: num = 1; a = 2;");
        
        // Can assign variables declared in outer scope
        testValid("""
                  var a: num = 1;
                  { a = 2; }
                  """);
        
        // Cannot assign variables declared in inner scope
        testInvalid("""
                    { var a: num = 1; }
                    a = 2;
                    """, UndefinedReferenceException.class);
        
        // Cannot assign variables declared in outer scope after reference
        testInvalid("""
                    { a = 2; }
                    var a: num = 1;
                    """, UndefinedReferenceException.class);
        
        // Cannot assign variable to expression of different type
        testInvalid("var a: num = 1; a = true;", InvalidTypeException.class);
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
    public void testFunctionReturnControlFlowAnalysis() {
        // Non-void returning function bodies must contain at least one statement that always returns
        testInvalid("""
                    fun returnsNum(): num {
                    }
                    """, InvalidFunctionDeclarationException.class);
        
        testValid("""
                  fun returnsNum(): num {
                      return 1;
                  }
                  """);
        
        testValid("""
                  fun returnsNum(): num {
                      1 + 1;
                      return 1;
                  }
                  """);
        
        // Statements after the first always-return-statement are unreachable, unreachable code is legal
        testValid("""
                  fun returnsNum(): num {
                      return 1;
                      1 + 1; // Unreachable
                  }
                  """);
        
        // Void returning functions can omit returns
        testValid("""
                  fun returnsNothing() {
                  }
                  """);
        
        // Returns can still be explicitly used for void returning functions
        testValid("""
                    fun returnsNum(p: bool) {
                        if (p) {
                            return;
                        }
                    }
                    """);
        
        // If all if-statement bodies always return, then the statement is considered as always returning
        testValid("""
                  fun returnsNum(p: bool): num {
                      if (p) {
                          return 1;
                      } else {
                          return 0;
                      }
                  }
                  """);
        
        testValid("""
                  fun returnsNum(p1: bool, p2: bool): num {
                      if (p1) {
                          return 1;
                      } else if (p2) {
                          return 2;
                      } else {
                          return 3;
                      }
                  }
                  """);
        
        // If no else body is defined, an if-statement does not always return, therefore the function does not contain any always-return-statements
        testInvalid("""
                    fun returnsNum(p: bool): num {
                        if (p) {
                            return 1;
                        }
                    }
                    """, InvalidFunctionDeclarationException.class);
        
        testInvalid("""
                    fun returnsNum(p1: bool, p2: bool): num {
                        if (p1) {
                            return 1;
                        } else if (p2) {
                            return 2;
                        }
                    }
                    """, InvalidFunctionDeclarationException.class);
        
        // Nested ifs should work
        testValid("""
                  fun returnsNum(p1: bool, p2: bool): num {
                      if (p1) {
                          if (p2) {
                              return 1;
                          } else {
                              return 2;
                          }
                      } else {
                          return 3;
                      }
                  }
                  """);
        
        testInvalid("""
                  fun returnsNum(p1: bool, p2: bool): num {
                      if (p1) {
                          if (p2) {
                              return 1;
                          }
                          // No else clause here, so will not always return
                      } else {
                          return 3;
                      }
                  }
                  """, InvalidFunctionDeclarationException.class);
        
        // If-statement may not always return but a return statement after it means function will always return
        testValid("""
                    fun returnsNum(p: bool): num {
                        if (p) {
                            return 1;
                        }
                        return 2;
                    }
                    """);
        
        // Same applies when statements are swapped
        testValid("""
                    fun returnsNum(p: bool): num {
                        return 1;
                        // Unreachable
                        if (p) {
                            return 2;
                        }
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
        
        // Cannot call non functions
        testInvalid("var test: num = 1; test();", InvalidCalleeException.class);
        
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
        testInvalid("var a: num = 1; fun doStuff() { a + 2; }", UndefinedReferenceException.class);
        testInvalid("fun doStuff() { a + 2; } var a: num = 1;", UndefinedReferenceException.class);
        
        // Type checking parameters
        testValid("fun takesNum(a: num) {} fun test(numberParam: num) { takesNum(numberParam); }");
        testInvalid("fun takesNum(a: num) {} fun test(boolParam: bool) { takesNum(boolParam); }", InvalidTypeException.class);
    }
    
    @Test
    public void testShadowing() {
        // Can't shadow variable in immediate scope
        testInvalid("var a: num = 1; var a: num = 2;", IdentifierAlreadyDeclaredException.class);
        
        // Can shadow variable from outer scope
        testValid("var a: num = 1; { var a: num = 2; }");
        
        // Can declare variable after same identifier is used previously in an inner scope
        testValid("{ var a: num = 2; } var a: num = 1;");
        
        // Ensure shadowed variable type is used
        testValid("""
                  fun takesNum(p: num) {}
                  var a: bool = false;
                  {
                      var a: num = 1;
                      takesNum(a);
                  }
                  """);
        
        // Should not be able to use type of shadowed variable in use of shadowing variable
        testInvalid("""
                  fun takesNum(p: num) {}
                  var a: num = false;
                  {
                      var a: bool = 1;
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
        testInvalid("var id: num = 1; fun id() {}", IdentifierAlreadyDeclaredException.class);
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
