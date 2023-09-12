package codegen;

import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Block;
import com.dfsek.terra.addons.terrascript.ast.TypedStmt;
import com.dfsek.terra.addons.terrascript.codegen.TerraScript;
import com.dfsek.terra.addons.terrascript.codegen.asm.TerraScriptClassGenerator;
import com.dfsek.terra.addons.terrascript.lexer.Lexer;
import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.semanticanalysis.SemanticAnalyzer;

import org.junit.jupiter.api.Test;

import java.util.Objects;


public class CodeGenTest {
    
    @Test
    public void test() {
        testValid("""
                  printNum(12345);
                  
                  if (1 == 1) print("Dis is true");
                  
                  num a = 1;
                  num b = 2;
                  str e = "test";
                  
                  if (a <= b) {
                      print("a is <= b");
                  } else {
                      print("a is not <= b");
                  }
                  
                  if (e == "foo") {
                      print("e is == foo");
                  } else if (e == "bar") {
                      print("e is == bar");
                  } else {
                      print("e is not foo or bar");
                  }
                  
                  if (true && false || (false && true)) {
                      print("Thin is tru");
                  } else {
                      print("Thin is not tru :(");
                  }
                  
                  fun loopTwiceThenBreak() {
                      num i = 0;
                      while (true) {
                          if (i == 2) break;
                          i = i + 1;
                      }
                  }
                  
                  loopTwiceThenBreak();
                  
                  retNum();
                  bool bln = true;
              
                  print(takesArgs("test", 3, true));
                  print(retStr());
                  
                  doStuff("Ayo", "world", true);
                  
                  fun retNum(): num {
                      return 3 + 3;
                  }
                  
                  fun retBool(): bool {
                      return true;
                  }
                  
                  fun concatThree(a: str, b: str, c: str): str {
                      fun concatTwo(a: str, b: str): str {
                          return a + b;
                      }
                      return concatTwo(a, b) + c;
                  }
                  
                  fun retStr(): str {
                      fun concatTwo(a: str, b: str): str {
                          return a + b;
                      }
                      str hello = "Hell";
                      hello = concatTwo(hello, "o");
                      str world = "world!";
                      return concatThree(hello, " ", world);
                  }
                  
                  fun takesArgs(a: str, b: num, c: bool): str {
                      return a;
                  }
                  
                  fun doStuff(a: str, b: str, c: bool) {
                      print("Doing stuff");
                      if (c) {
                          print(concatThree(a, " ", b));
                      } else {
                          print("c is false");
                      }
                  }
                  """);
    }
    
    private void testValid(String validSource) {
        try {
            Block script = Parser.parse(new Lexer(validSource).analyze());
            TypedStmt.Block typedScript = SemanticAnalyzer.analyze(script, new ErrorHandler());
            TerraScript ts = new TerraScriptClassGenerator("./build/codegentest").generate(typedScript);
            ts.execute();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
