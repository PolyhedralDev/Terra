import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.config.preprocessor.MetaListLikePreprocessor;
import com.dfsek.terra.config.preprocessor.MetaMapPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaNumberPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaStringPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaValuePreprocessor;


public class MetaTest {
    @Test
    public void testMetaList() {
        Configuration meta = new YamlConfiguration(MetaTest.class.getResourceAsStream("/meta.yml"), "meta.yml");
        Configuration metaTarget = new YamlConfiguration(MetaTest.class.getResourceAsStream("/metaTarget.yml"), "metaTarget.yml");
        
        Map<String, Configuration> configurationMap = new HashMap<>();
        
        configurationMap.put(meta.getName(), meta);
        configurationMap.put(metaTarget.getName(), metaTarget);
        
        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(Meta.class, new MetaStringPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaListLikePreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaMapPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaNumberPreprocessor(configurationMap));
        
        loader.registerPreprocessor(Meta.class, new MetaValuePreprocessor(configurationMap));
        
        loader.load(new MetaListConfig(), meta).list.forEach(System.out::println);
    }
    
    @Test
    public void testMetaMap() {
        Configuration meta = new YamlConfiguration(MetaTest.class.getResourceAsStream("/meta.yml"), "meta.yml");
        Configuration metaTarget = new YamlConfiguration(MetaTest.class.getResourceAsStream("/metaTarget.yml"), "metaTarget.yml");
        
        Map<String, Configuration> configurationMap = new HashMap<>();
        
        configurationMap.put(meta.getName(), meta);
        configurationMap.put(metaTarget.getName(), metaTarget);
        
        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(Meta.class, new MetaStringPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaListLikePreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaMapPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaNumberPreprocessor(configurationMap));
        
        loader.registerPreprocessor(Meta.class, new MetaValuePreprocessor(configurationMap));
        
        loader.load(new MetaMapConfig(), meta).map.forEach((k, v) -> System.out.println(k + ": " + v));
    }
    
    @Test
    public void testMetaString() {
        Configuration meta = new YamlConfiguration(MetaTest.class.getResourceAsStream("/meta.yml"), "meta.yml");
        Configuration metaTarget = new YamlConfiguration(MetaTest.class.getResourceAsStream("/metaTarget.yml"), "metaTarget.yml");
        
        Map<String, Configuration> configurationMap = new HashMap<>();
        
        configurationMap.put(meta.getName(), meta);
        configurationMap.put(metaTarget.getName(), metaTarget);
        
        ConfigLoader loader = new ConfigLoader();
        
        loader.registerPreprocessor(Meta.class, new MetaStringPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaListLikePreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaMapPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaNumberPreprocessor(configurationMap));
        
        loader.registerPreprocessor(Meta.class, new MetaValuePreprocessor(configurationMap));
        
        System.out.println(loader.load(new MetaStringConfig(), meta).string);
    }
    
    @Test
    public void testMetaNumber() {
        Configuration meta = new YamlConfiguration(MetaTest.class.getResourceAsStream("/meta.yml"), "meta.yml");
        Configuration metaTarget = new YamlConfiguration(MetaTest.class.getResourceAsStream("/metaTarget.yml"), "metaTarget.yml");
        
        Map<String, Configuration> configurationMap = new HashMap<>();
        
        configurationMap.put(meta.getName(), meta);
        configurationMap.put(metaTarget.getName(), metaTarget);
        
        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(Meta.class, new MetaStringPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaListLikePreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaMapPreprocessor(configurationMap));
        loader.registerPreprocessor(Meta.class, new MetaNumberPreprocessor(configurationMap));
        
        loader.registerPreprocessor(Meta.class, new MetaValuePreprocessor(configurationMap));
        
        System.out.println("int: " + loader.load(new MetaNumberConfig(), meta).integer);
        System.out.println("double: " + loader.load(new MetaNumberConfig(), meta).aDouble);
    }
    
    
    private static final class MetaListConfig implements ConfigTemplate {
        @Value("list")
        private @Meta List<@Meta String> list;
    }
    
    
    private static final class MetaMapConfig implements ConfigTemplate {
        @Value("map")
        private @Meta Map<@Meta String, @Meta String> map;
    }
    
    
    private static final class MetaStringConfig implements ConfigTemplate {
        @Value("string")
        private @Meta String string;
    }
    
    
    private static final class MetaNumberConfig implements ConfigTemplate {
        @Value("int")
        private @Meta int integer;
        
        @Value("double")
        private @Meta double aDouble;
    }
}
