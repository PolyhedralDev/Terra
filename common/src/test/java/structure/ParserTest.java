package structure;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.Argument;
import com.dfsek.terra.api.structures.parser.Function;
import com.dfsek.terra.api.structures.parser.FunctionBuilder;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParserTest {
    @Test
    public void parse() throws IOException, ParseException {
        Parser parser = new Parser(IOUtils.toString(getClass().getResourceAsStream("/test.tesf")));

        parser.addFunction("test", new FunctionBuilder<Test1>() {
            @Override
            public Test1 build(List<String> argumentList) throws ParseException {
                return new Test1(argumentList.get(0), Double.parseDouble(argumentList.get(1)));
            }

            @Override
            public List<Argument<?>> getArguments() {
                return Arrays.asList(id -> id, Double::parseDouble);
            }
        });

        List<Function> functions = parser.parse();

        for(Function f : functions) {
            System.out.println(f);
        }
    }

    private static class Test1 implements Function {
        private final String a;
        private final double b;

        public Test1(String a, double b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public double getB() {
            return b;
        }

        @Override
        public void apply(Location location) {

        }

        @Override
        public void apply(Location location, Chunk chunk) {

        }

        @Override
        public String name() {
            return null;
        }
    }
}
