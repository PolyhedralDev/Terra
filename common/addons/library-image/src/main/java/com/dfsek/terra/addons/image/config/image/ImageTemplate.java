package com.dfsek.terra.addons.image.config.image;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.io.IOException;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;


public class ImageTemplate implements ObjectTemplate<Image> {
    
    @Value("path")
    private String path;
    
    private final Loader files;
    
    private final ConfigPack pack;
    
    public ImageTemplate(Loader files, ConfigPack pack) {
        this.files = files;
        this.pack = pack;
    }
    
    @Override
    public Image get() {
        try {
            return ImageCache.load(path, pack, files);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
