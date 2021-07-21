package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

public abstract class MutatorStageTemplate implements ObjectTemplate<BiomeMutator> {
    @Value("noise")
    protected @Meta NoiseSampler noise;

    @Override
    public abstract BiomeMutator get();
}
