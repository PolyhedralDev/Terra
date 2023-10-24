package com.dfsek.terra.addons.image.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.properties.Properties;

public class ImageLibraryPackConfigTemplate implements ConfigTemplate, Properties {
    // TODO - These would be better as plugin wide config parameters in config.yml
    
    @Value("images.cache.load-on-use")
    @Description("If set to true, images will load into memory upon use rather than on pack load.")
    @Default
    private boolean loadOnUse = false;
    
    @Value("images.cache.unload-on-timeout")
    @Description("If set to true, images will be removed from memory if not used after a timeout, otherwise images will stay loaded in memory. " +
                 "Trades decreased memory consumption when not performing any image reads for a period of time for extra processing time required to perform cache lookups.")
    @Default
    private boolean unloadOnTimeout = false;
    
    @Value("images.cache.timeout")
    @Description("How many seconds to keep images loaded in the image cache for if images.cache.unload-on-timeout is enabled.")
    @Default
    private int cacheTimeout = 300;
    
    public boolean loadOnUse() {
        return loadOnUse;
    }
    
    public boolean unloadOnTimeout() {
        return unloadOnTimeout;
    }
    
    public int getCacheTimeout() {
        return cacheTimeout;
    }
}
