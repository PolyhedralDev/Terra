package com.dfsek.terra.addons.numberpredicate;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.function.DoublePredicate;


public class DoublePredicateLoader implements TypeLoader<DoublePredicate> {
    @Override
    public DoublePredicate load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader,
                                DepthTracker depthTracker) throws LoadException {
        if (o instanceof String expressionString) {
            Scope scope = new Scope();
            scope.addInvocationVariable("value");
            try {
                Expression expression = new Parser().parse(expressionString, scope);
                return d -> expression.evaluate(d) != 0; // Paralithic expressions treat '!= 0' as true
            } catch(ParseException e) {
                throw new LoadException("Failed to parse double predicate expression", e, depthTracker);
            }
        } else {
            throw new LoadException("Double predicates must be defined as a string. E.g. 'value > 3'", depthTracker);
        }
    }
}
