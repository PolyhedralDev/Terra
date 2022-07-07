/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package structure;


import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Executable;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class ParserTest {
    @Test
    public void parse() throws IOException, ParseException {
        Parser parser = new Parser(
                IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/test.tesf")), Charset.defaultCharset()));
        
        parser.registerFunction("test", new FunctionBuilder<Test1>() {
            @Override
            public Test1 build(List<Returnable<?>> argumentList, Position position) {
                return new Test1(argumentList.get(0), argumentList.get(1), position);
            }
            
            @Override
            public int argNumber() {
                return 2;
            }
            
            @Override
            public Returnable.ReturnType getArgument(int position) {
                return switch(position) {
                    case 0 -> Returnable.ReturnType.STRING;
                    case 1 -> Returnable.ReturnType.NUMBER;
                    default -> null;
                };
            }
            
        });
        
        long l = System.nanoTime();
        Executable block = parser.parse();
        long t = System.nanoTime() - l;
        System.out.println("Took " + (double) t / 1000000);
        
        block.execute(null);
        
        block.execute(null);
    }
    
    private static class Test1 implements Function<Void> {
        private final Returnable<?> a;
        private final Returnable<?> b;
        private final Position position;
        
        public Test1(Returnable<?> a, Returnable<?> b, Position position) {
            this.a = a;
            this.b = b;
            this.position = position;
        }
        
        @Override
        public Void apply(ImplementationArguments implementationArguments, Scope scope) {
            System.out.println("string: " + a.apply(implementationArguments, scope) + ", double: " +
                               b.apply(implementationArguments, scope));
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
