package com.dfsek.terra.addons.image.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.addons.image.image.BufferedImageWrapper;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.api.config.Loader;


public class ImageLoader implements TypeLoader<Image> {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageLoader.class);
    
    private final Loader files;
    
    public ImageLoader(Loader files) {
        this.files = files;
    }
    
    @Override
    public Image load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        try {
            return new BufferedImageWrapper(ImageIO.read(files.get((String) c)));
        } catch(IllegalArgumentException e) {
            throw new LoadException("Unable to load image (image might be too large?)", e, depthTracker);
        } catch(IOException e) {
            throw new LoadException("Unable to load image", e, depthTracker);
        }
    }
}
