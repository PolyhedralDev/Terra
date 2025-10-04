package com.dfsek.terra.addons.image.config.image;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import com.dfsek.terra.addons.image.config.ImageLibraryPackConfigTemplate;
import com.dfsek.terra.addons.image.image.BufferedImageWrapper;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.image.SuppliedImage;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.util.generic.Lazy;

import static com.dfsek.terra.api.util.cache.CacheUtils.CACHE_EXECUTOR;


/*
 * Cache prevents configs from loading the same image multiple times into memory
 */
record ImageCache(LoadingCache<String, Image> cache) implements Properties {
    public static Image load(String path, ConfigPack pack) throws IOException {
        ImageLibraryPackConfigTemplate config = pack.getContext().get(ImageLibraryPackConfigTemplate.class);
        ImageCache images;
        if(!pack.getContext().has(ImageCache.class)) {
            var cacheBuilder = Caffeine.newBuilder().executor(CACHE_EXECUTOR).scheduler(Scheduler.systemScheduler());
            if(config.unloadOnTimeout()) cacheBuilder.expireAfterAccess(config.getCacheTimeout(), TimeUnit.SECONDS);
            images = new ImageCache(cacheBuilder.build(s -> loadImage(s, pack.getRootPath())));
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

    private static Image loadImage(String path, Path directory) throws IOException {
        InputStream is = Files.newInputStream(directory.resolve(path));
        return new BufferedImageWrapper(ImageIO.read(is));
    }
}
