package noise;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;


@SuppressWarnings("unused")
public class NoiseConfigTemplate implements ConfigTemplate {
    @Value(".")
    private NoiseSeeded builder;

    public NoiseSeeded getBuilder() {
        return builder;
    }
}
