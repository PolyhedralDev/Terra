import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.config.preprocessor.MetaListPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaValuePreprocessor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaTest {
    @Test
    public void testMetaList() {
        Configuration meta = new YamlConfiguration(MetaTest.class.getResourceAsStream("/meta.yml"), "meta.yml");
        Configuration metaTarget = new YamlConfiguration(MetaTest.class.getResourceAsStream("/metaTarget.yml"), "metaTarget.yml");

        Map<String, Configuration> configurationMap = new HashMap<>();

        configurationMap.put(meta.getName(), meta);
        configurationMap.put(metaTarget.getName(), metaTarget);

        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(Meta.class, new MetaValuePreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaListPreprocessor(configurationMap));

        loader.load(new ConfigTest(), meta).list.forEach(System.out::println);
    }

    private static final class ConfigTest implements ConfigTemplate {
        @Value("list")
        private @Meta List<@Meta String> list;
    }
}