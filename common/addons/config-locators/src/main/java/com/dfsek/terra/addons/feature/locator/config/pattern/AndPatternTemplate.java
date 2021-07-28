package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.config.meta.Meta;

import java.util.List;

public class AndPatternTemplate implements ObjectTemplate<Pattern>, ValidatedConfigTemplate {
    @Value("patterns")
    private @Meta List<@Meta Pattern> patterns;

    @Override
    public Pattern get() {
        Pattern current = patterns.remove(0);
        while(!patterns.isEmpty()) {
            current = current.and(patterns.remove(0));
        }
        return current;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(patterns.isEmpty()) throw new ValidationException("AND Pattern must specify at least 1 pattern.");
        return true;
    }
}
