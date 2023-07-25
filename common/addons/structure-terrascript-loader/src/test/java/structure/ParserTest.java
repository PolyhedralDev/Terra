/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package structure;


import com.dfsek.terra.addons.terrascript.parser.lang.Scope.ScopeBuilder;

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
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class ParserTest {
    @Test
    public void parse() throws IOException, ParseException {
        Parser parser = new Parser(
                IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/test.tesf")), Charset.defaultCharset()));
        
        ScopeBuilder scope = new ScopeBuilder();
        scope.registerFunction("test", new FunctionBuilder<Test1>() {
            @Override
            public Test1 build(List<Expression<?>> argumentList, SourcePosition position) {
                return new Test1(argumentList.get(0), argumentList.get(1), position);
            }
            
            @Override
            public int argNumber() {
                return 2;
            }
            
            @Override
            public Expression.ReturnType getArgument(int position) {
                return switch(position) {
                    case 0 -> Expression.ReturnType.STRING;
                    case 1 -> Expression.ReturnType.NUMBER;
                    default -> null;
                };
            }
            
        });
        
        long l = System.nanoTime();
        Executable block = parser.parse(scope);
        long t = System.nanoTime() - l;
        System.out.println("Took " + (double) t / 1000000);
        
        block.execute(null);
        
        block.execute(null);
    }
    
    private static class Test1 implements Function<Void> {
        private final Expression<?> a;
        private final Expression<?> b;
        private final SourcePosition position;
        
        public Test1(Expression<?> a, Expression<?> b, SourcePosition position) {
            this.a = a;
            this.b = b;
            this.position = position;
        }
        
        @Override
        public Void evaluate(ImplementationArguments implementationArguments, Scope scope) {
            System.out.println("string: " + a.evaluate(implementationArguments, scope) + ", double: " +
                               b.evaluate(implementationArguments, scope));
            return null;
        }
        
        @Override
        public SourcePosition getPosition() {
            return position;
        }
        
        @Override
        public ReturnType returnType() {
            return ReturnType.VOID;
        }
    }
}
