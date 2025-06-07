package com.dfsek.terra.addons.chunkgenerator.math.pointset.operative;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;

import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.seismic.type.vector.Vector3Int;


public class ExpressionFilterPointSet implements PointSet {
    
    private final Stream<Vector3Int> points;
    
    public ExpressionFilterPointSet(PointSet set, String eq) throws ParseException {
        Parser parser = new Parser();
        Scope scope = new Scope();
        scope.addInvocationVariable("x");
        scope.addInvocationVariable("y");
        scope.addInvocationVariable("z");
        Expression expression = parser.parse(eq, scope);
        this.points = set.get().filter(v -> expression.evaluate(v.getX(), v.getY(), v.getZ()) == 1);
    }
    
    @Override
    public Stream<Vector3Int> get() {
        return points;
    }
}
