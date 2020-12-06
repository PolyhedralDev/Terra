package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.image.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ImageLoaderLoader implements TypeLoader<ImageLoader> {
    @Override
    public ImageLoader load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        File image = new File((String) map.get("file"));
        try {
            return new ImageLoader(image, (ImageLoader.Align) configLoader.loadType(ImageLoader.Align.class, map.get("align")));
        } catch(IOException e) {
            throw new LoadException("Unable to load image", e);
        }
    }
}
