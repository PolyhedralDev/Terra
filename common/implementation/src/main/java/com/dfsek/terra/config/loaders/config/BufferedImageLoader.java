package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.config.Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;

public class BufferedImageLoader implements TypeLoader<BufferedImage> {
    private final Loader files;

    public BufferedImageLoader(Loader files) {
        this.files = files;
    }

    @Override
    public BufferedImage load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        try {
            return ImageIO.read(files.get((String) c));
        } catch(IOException e) {
            throw new LoadException("Unable to load image", e);
        }
    }
}
