/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package legacy;


import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.lexer.Lexer;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope.ScopeBuilder;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.dfsek.terra.addons.terrascript.legacy.parser.Parser;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Executable;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class ParserTest {
    @Test
    public void parse() throws IOException, ParseException {
        Lexer lexer = new Lexer(IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/test.tesf")), Charset.defaultCharset()));
        Parser parser = new Parser(lexer);
        
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
            public Type getArgument(int position) {
                return switch(position) {
                    case 0 -> Type.STRING;
                    case 1 -> Type.NUMBER;
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
        public Type returnType() {
            return Type.VOID;
        }
    }
}
