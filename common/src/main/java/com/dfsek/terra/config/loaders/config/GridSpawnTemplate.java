package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.GridSpawn;

@SuppressWarnings("FieldMayBeFinal")
public class GridSpawnTemplate implements ObjectTemplate<GridSpawn> {
    @Value("width")
    private MetaValue<Integer> width;

    @Value("padding")
    private MetaValue<Integer> padding;

    @Value("salt")
    @Default
    private MetaValue<Integer> salt = MetaValue.of(0);

    @Override
    public GridSpawn get() {
        return new GridSpawn(width.get(), padding.get(), salt.get());
    }
}
