package com.dfsek.terra.addons.image.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.api.config.Loader;


public class BufferedImageLoader implements TypeLoader<BufferedImage> {
    private final Loader files;
    
    public BufferedImageLoader(Loader files) {
        this.files = files;
    }
    
    @Override
    public BufferedImage load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        try {
            return ImageIO.read(files.get((String) c));
        } catch(IOException e) {
            throw new LoadException("Unable to load image", e, depthTracker);
        }
    }
}
