package com.dfsek.terra.addons.image.config.image;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.dfsek.terra.addons.image.image.BufferedImageWrapper;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.properties.Properties;


/*
 * Cache prevents configs from loading the same image multiple times into memory
 */
record ImageCache(ConcurrentHashMap<String, Image> map) implements Properties {
    public static Image load(String path, ConfigPack pack, Loader files) throws IOException {
        ImageCache cache;
        if(!pack.getContext().has(ImageCache.class)) {
            cache = new ImageCache(new ConcurrentHashMap<>());
            pack.getContext().put(cache);
        } else {
            cache = pack.getContext().get(ImageCache.class);
        }
        
        if(cache.map.containsKey(path)) {
            return cache.map.get(path);
        } else {
            try {
                BufferedImageWrapper image = new BufferedImageWrapper(ImageIO.read(files.get(path)));
                cache.map.put(path, image);
                return image;
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
}
