package com.dfsek.terra.config;

import com.dfsek.terra.api.TerraPlugin;

public class WrapperPluginConfig extends PluginConfig {
    private final String language;
    private final boolean debug;
    private final long dataSaveInterval = 0L;
    private final int biomeSearchResolution;
    private final int carverCacheSize;
    private final int structureCache;
    private final int samplerCache;
    private final int maxRecursions;

    protected WrapperPluginConfig(String language, boolean debug, int biomeSearchResolution, int carverCacheSize, int structureCache, int samplerCache, int maxRecursions) {
        this.language = language;
        this.debug = debug;
        this.biomeSearchResolution = biomeSearchResolution;
        this.carverCacheSize = carverCacheSize;
        this.structureCache = structureCache;
        this.samplerCache = samplerCache;
        this.maxRecursions = maxRecursions;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void load(TerraPlugin main) {
        // do nothing
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public long getDataSaveInterval() {
        return dataSaveInterval;
    }

    @Override
    public int getBiomeSearchResolution() {
        return biomeSearchResolution;
    }

    @Override
    public int getCarverCacheSize() {
        return carverCacheSize;
    }

    @Override
    public int getStructureCache() {
        return structureCache;
    }

    @Override
    public int getSamplerCache() {
        return samplerCache;
    }

    @Override
    public int getMaxRecursion() {
        return maxRecursions;
    }

    @Override
    public String toString() {
        return "WrapperPluginConfig{" +
                "language='" + language + '\'' +
                ", debug=" + debug +
                ", dataSaveInterval=" + dataSaveInterval +
                ", biomeSearchResolution=" + biomeSearchResolution +
                ", carverCacheSize=" + carverCacheSize +
                ", structureCache=" + structureCache +
                ", samplerCache=" + samplerCache +
                ", maxRecursions=" + maxRecursions +
                '}';
    }

    public static final class Builder {
        private String language = "en_us";
        private boolean debug = false;
        private int biomeSearchResolution = 4;
        private int carverCacheSize = 512;
        private int structureCache = 128;
        private int samplerCache = 512;
        private int maxRecursions = 1000;

        private Builder() {
        }

        public static Builder aWrapperPluginConfig() {
            return new Builder();
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setBiomeSearchResolution(int biomeSearchResolution) {
            this.biomeSearchResolution = biomeSearchResolution;
            return this;
        }

        public Builder setCarverCacheSize(int carverCacheSize) {
            this.carverCacheSize = carverCacheSize;
            return this;
        }

        public Builder setStructureCache(int structureCache) {
            this.structureCache = structureCache;
            return this;
        }

        public Builder setSamplerCache(int samplerCache) {
            this.samplerCache = samplerCache;
            return this;
        }

        public Builder setMaxRecursions(int maxRecursions) {
            this.maxRecursions = maxRecursions;
            return this;
        }

        public WrapperPluginConfig build() {
            return new WrapperPluginConfig(language, debug, biomeSearchResolution, carverCacheSize, structureCache, samplerCache, maxRecursions);
        }
    }
}
