package com.dfsek.terra.config.loaders;

import com.dfsek.paralithic.eval.parser.Parser.ParseOptions;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;


public class ExpressionParserOptionsTemplate implements ObjectTemplate<ParseOptions> {

    private static final ParseOptions DEFAULT_PARSE_OPTIONS = new ParseOptions();

    @Value("use-let-expressions")
    @Default
    private boolean useLetExpressions = DEFAULT_PARSE_OPTIONS.useLetExpressions();
    
    @Override
    public ParseOptions get() {
        return new ParseOptions(useLetExpressions);
    }
}
