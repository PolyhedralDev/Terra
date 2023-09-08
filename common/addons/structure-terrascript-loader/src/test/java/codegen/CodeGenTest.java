package codegen;

import com.dfsek.terra.addons.terrascript.ErrorHandler;
import com.dfsek.terra.addons.terrascript.ast.Stmt.Block;
import com.dfsek.terra.addons.terrascript.codegen.TerraScript;
import com.dfsek.terra.addons.terrascript.codegen.asm.TerraScriptClassGenerator;
import com.dfsek.terra.addons.terrascript.lexer.Lexer;
import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.semanticanalysis.SemanticAnalyzer;

import org.junit.jupiter.api.Test;

public class CodeGenTest {
    
    @Test
    public void test() {
        testValid("""
                  fun retNum(): num {
                      return 3 + 3;
                  }
                  
                  fun retBool(): bool {
                      return true;
                  }
                  
                  fun concatThree(a: str, b: str, c: str): str {
                      return a + b + c;
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
                  
                  num a = 1;
                  num b = 2;
                  str e = "test";
                  
                  retNum();
                  bool bln = true;
              
                  print(takesArgs("test", 3, true));
                  print(retStr());
                  
                  doStuff("Ay0o", "world", true);
                  """);
    }
    
    private void testValid(String validSource) {
        try {
            Block script = Parser.parse(new Lexer(validSource).analyze());
            SemanticAnalyzer.analyze(script, new ErrorHandler());
            TerraScript ts = new TerraScriptClassGenerator("./build/codegentest").generate(script);
            ts.execute();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
