package com.dfsek.terra.config.templates.ore;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Value;
import org.polydev.gaea.math.Range;

@SuppressWarnings("unused")
public class VanillaOreTemplate {
    @Value("size")
    @Abstractable
    private Range size;

    public Range getSize() {
        return size;
    }
}
