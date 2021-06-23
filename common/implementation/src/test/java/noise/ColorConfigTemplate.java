package noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;

public class ColorConfigTemplate implements ConfigTemplate {
    @Value("colors")
    private ProbabilityCollectionImpl<Integer> colors;

    @Value("enable")
    @Default
    private boolean enable = false;

    public boolean enable() {
        return enable;
    }

    public ProbabilityCollectionImpl<Integer> getColors() {
        return colors;
    }
}
