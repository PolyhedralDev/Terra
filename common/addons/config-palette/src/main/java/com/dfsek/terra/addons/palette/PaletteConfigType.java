package com.dfsek.terra.addons.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeLoader;

import java.util.function.Supplier;

import com.dfsek.terra.addons.palette.palette.PaletteImpl;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.generator.Palette;


public class PaletteConfigType implements ConfigType<PaletteTemplate, Palette> {
    public static final TypeKey<Palette> PALETTE_TYPE_TOKEN = new TypeKey<>() {
    };
    private final PaletteFactory factory = new PaletteFactory();
    private final TerraPlugin main;
    
    public PaletteConfigType(TerraPlugin main) {
        this.main = main;
    }
    
    @Override
    public Supplier<OpenRegistry<Palette>> registrySupplier(ConfigPack pack) {
        return () -> pack.getRegistryFactory().create(registry -> (TypeLoader<Palette>) (t, c, loader) -> {
            if(((String) c).startsWith("BLOCK:"))
                return new PaletteImpl.Singleton(
                        main.getWorldHandle().createBlockData(((String) c).substring(6))); // Return single palette for BLOCK: shortcut.
            Palette obj = registry.get((String) c);
            if(obj == null)
                throw new LoadException("No such " + t.getType().getTypeName() + " matching \"" + c + "\" was found in this registry.");
            return obj;
        });
    }
    
    @Override
    public PaletteTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new PaletteTemplate();
    }
    
    @Override
    public ConfigFactory<PaletteTemplate, Palette> getFactory() {
        return factory;
    }
    
    @Override
    public TypeKey<Palette> getTypeKey() {
        return PALETTE_TYPE_TOKEN;
    }
}
