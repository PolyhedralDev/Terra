package com.dfsek.terra.config.templates.ore;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import org.polydev.gaea.math.Range;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class DeformedSphereOreTemplate implements ConfigTemplate {

    @Value("radius.min")
    @Abstractable
    private Range size;

    @Value("deform")
    @Abstractable
    @Default
    private double deform = 0.75D;

    @Value("deform-frequency")
    @Abstractable
    @Default
    private double deformFrequency = 0.1D;

    public double getDeform() {
        return deform;
    }

    public double getDeformFrequency() {
        return deformFrequency;
    }

    public Range getSize() {
        return size;
    }
}
