package noise;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.world.generation.config.NoiseBuilder;


@SuppressWarnings("unused")
public class NoiseConfigTemplate implements ConfigTemplate {
    @Value(".")
    private NoiseBuilder builder;

    public NoiseBuilder getBuilder() {
        return builder;
    }
}
