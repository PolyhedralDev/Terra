package com.dfsek.terra.addons.image.config.image;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.api.config.ConfigPack;

import java.io.IOException;


public class ImageTemplate implements ObjectTemplate<Image> {

    private final ConfigPack pack;
    @Value("path")
    private String path;

    public ImageTemplate(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public Image get() {
        try {
            return ImageCache.load(path, pack);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
