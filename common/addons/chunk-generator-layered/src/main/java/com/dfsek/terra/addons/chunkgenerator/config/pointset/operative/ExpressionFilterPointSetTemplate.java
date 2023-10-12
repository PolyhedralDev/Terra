package com.dfsek.terra.addons.chunkgenerator.config.pointset.operative;

import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.operative.ExpressionFilterPointSet;


public class ExpressionFilterPointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Value("point-set")
    private PointSet set;
    
    @Value("expression")
    private String expression;
    
    @Override
    public PointSet get() {
        try {
            return new ExpressionFilterPointSet(set, expression);
        } catch(ParseException e) {
            throw new RuntimeException("Failed to parse expression.", e);
        }
    }
}
