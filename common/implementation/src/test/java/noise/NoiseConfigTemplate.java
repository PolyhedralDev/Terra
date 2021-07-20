package noise;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;


@SuppressWarnings("unused")
public class NoiseConfigTemplate implements ConfigTemplate {
    @Value(".")
    private NoiseSampler builder;

    public NoiseSampler getBuilder() {
        return builder;
    }
}
