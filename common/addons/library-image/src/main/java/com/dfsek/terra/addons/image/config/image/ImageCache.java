package com.dfsek.terra.addons.image.config.image;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.dfsek.terra.addons.image.config.ImageLibraryPackConfigTemplate;
import com.dfsek.terra.addons.image.image.BufferedImageWrapper;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.image.SuppliedImage;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.util.generic.Lazy;


/*
 * Cache prevents configs from loading the same image multiple times into memory
 */
record ImageCache(LoadingCache<String, Image> cache) implements Properties {
    public static Image load(String path, ConfigPack pack, Loader files) throws IOException {
        ImageLibraryPackConfigTemplate config = pack.getContext().get(ImageLibraryPackConfigTemplate.class);
        ImageCache images;
        if(!pack.getContext().has(ImageCache.class)) {
            var cacheBuilder = Caffeine.newBuilder();
            if(config.unloadOnTimeout()) cacheBuilder.expireAfterAccess(config.getCacheTimeout(), TimeUnit.SECONDS);
            images = new ImageCache(cacheBuilder.build(s -> loadImage(s, files)));
            pack.getContext().put(images);
        } else images = pack.getContext().get(ImageCache.class);
        
        if(config.loadOnUse()) {
            if(config.unloadOnTimeout()) { // Grab directly from cache if images are to unload on timeout
                return new SuppliedImage(() -> images.cache.get(path));
            } else {
                // If images do not time out, image can be lazily loaded once instead of performing cache lookups for each image operation
                Lazy<Image> lazyImage = Lazy.lazy(() -> images.cache.get(path));
                return new SuppliedImage(lazyImage::value);
            }
        }
        
        return images.cache.get(path);
    }
    
    private static Image loadImage(String path, Loader files) throws IOException {
        try {
            return new BufferedImageWrapper(ImageIO.read(files.get(path)));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Unable to load image (image might be too large?)", e);
        } catch(IOException e) {
            if(e instanceof FileNotFoundException) {
                // Rethrow using nicer message
                throw new IOException("Unable to load image: No such file or directory: " + path, e);
            }
            throw new IOException("Unable to load image", e);
        }
    }
}
