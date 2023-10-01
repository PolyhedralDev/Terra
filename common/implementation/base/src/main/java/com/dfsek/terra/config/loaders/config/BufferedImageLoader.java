/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.util.concurrent.ConcurrentHashMap;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.properties.Properties;

/*
 * @deprecated Use the Image and ImageLoader class provided by the library-image addon instead. This is subject to removal in v7.
 */
@Deprecated
public class BufferedImageLoader implements TypeLoader<BufferedImage> {
    private final Loader files;
    
    private final ConfigPack pack;
    
    public BufferedImageLoader(Loader files, ConfigPack pack) {
        this.files = files;
        this.pack = pack;
        if (!pack.getContext().has(ImageCache.class))
            pack.getContext().put(new ImageCache(new ConcurrentHashMap<>()));
    }
    
    @Override
    public BufferedImage load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        return pack.getContext().get(ImageCache.class).map.computeIfAbsent((String) c, s -> {
            try {
                return ImageIO.read(files.get(s));
            } catch(IOException e) {
                throw new LoadException("Unable to load image", e, depthTracker);
            }
        });
    }
    
    /*
     * Cache prevents configs from loading the same image multiple times into memory
     */
    private record ImageCache(ConcurrentHashMap<String, BufferedImage> map) implements Properties {
    }
}
