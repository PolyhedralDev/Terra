package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import org.checkerframework.checker.units.qual.A;

import java.time.Duration;
import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate extends AbstractableTemplate implements ValidatedConfigTemplate {
    @Value("id")
    private String id;

    @Value("scripts")
    @Abstractable
    private ProbabilityCollection<StructureScript> structure;

    @Value("spawn.start")
    @Abstractable
    private Range y;

    @Value("spawn")
    @Abstractable
    private GridSpawn spawn;

    @Value("features")
    @Abstractable
    @Default
    private List<Void> features = new GlueList<>();

    @Value("repeat.scripts")
    @Default
    @Abstractable
    private ProbabilityCollection<StructureScript> repeatScripts = new ProbabilityCollection<>();

    @Value("repeat.ticks.min")
    @Default
    @Abstractable
    private long minRepeat = 20;

    @Value("repeat.ticks.max")
    @Default
    @Abstractable
    private long maxRepeat = 20;

    public String getID() {
        return id;
    }

    public ProbabilityCollection<StructureScript> getStructures() {
        return structure;
    }

    public Range getY() {
        return y;
    }

    public List<Void> getFeatures() {
        return features;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(maxRepeat < minRepeat) throw new ValidationException("repeat.ticks.max must be greater than repeat.ticks.min");
        return true;
    }
}
