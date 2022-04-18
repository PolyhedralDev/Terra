package structure;

import net.jafama.FastMath;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.UnaryNumberFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.UnaryStringFunctionBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class PerformanceTest {
    public static void main(String... args) throws IOException {
        Parser parser = new Parser(
                IOUtils.toString(PerformanceTest.class.getResourceAsStream("/performance.tesf"), Charset.defaultCharset()));
        
        parser.registerFunction("assert", new FunctionBuilder<AssertFunction>() {
                  @Override
                  public AssertFunction build(List<Returnable<?>> argumentList, Position position) {
                      return new AssertFunction(position, (Returnable<Boolean>) argumentList.get(0));
                  }
            
                  @Override
                  public int argNumber() {
                      return 1;
                  }
            
                  @Override
                  public Returnable.ReturnType getArgument(int position) {
                      return switch(position) {
                          case 0 -> ReturnType.BOOLEAN;
                          default -> null;
                      };
                  }
            
              }).registerFunction("print",
                                  new UnaryStringFunctionBuilder(System.out::println))
              .registerFunction("sqrt", new UnaryNumberFunctionBuilder(number -> FastMath.sqrt(number.doubleValue())));
        
        Block block = parser.parse();
        
        for(int i = 0; i < 20; i++) {
            long s = System.nanoTime();
            
            block.apply(null, new Scope());
            
            long e = System.nanoTime();
            long d = e - s;
            
            System.out.println("Took " + ((double) d) / 1000000 + "ms");
        }
    }
    
    public static final class AssertFunction implements Function<Void> {
        private final Position position;
        private final Returnable<Boolean> arg;
        
        public AssertFunction(Position position, Returnable<Boolean> arg) {
            this.position = position;
            this.arg = arg;
        }
        
        @Override
        public Void apply(ImplementationArguments implementationArguments, Scope scope) {
            if(!arg.apply(implementationArguments, scope)) throw new IllegalStateException();
            return null;
        }
        
        @Override
        public Position getPosition() {
            return position;
        }
        
        @Override
        public ReturnType returnType() {
            return ReturnType.VOID;
        }
    }
}
