package com.dfsek.terra.config.pack;

import com.dfsek.paralithic.eval.parser.Parser.ParseOptions;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;


public class ConfigPackExpressionOptionsTemplate implements ConfigTemplate {
    @Value("expressions.options")
    @Default
    private ParseOptions parseOptions = new ParseOptions();

    public ParseOptions getParseOptions() {
        return parseOptions;
    }
}
