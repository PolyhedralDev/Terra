package com.dfsek.terra.config.base;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.Terra;
import com.dfsek.terra.image.ImageLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class WorldConfig implements ValidatedConfigTemplate {
    private static final ConfigLoader LOADER = new ConfigLoader();

    private final String worldID;
    private final String configID;
    @Value("image.enable")
    @Default
    public boolean fromImage = false;

    @Value("image.channels.biome-x")
    @Default
    public ImageLoader.Channel biomeXChannel = ImageLoader.Channel.RED;
    @Value("image.channels.biome-z")
    @Default
    public ImageLoader.Channel biomeZChannel = ImageLoader.Channel.GREEN;
    @Value("image.channels.zone")
    @Default
    public ImageLoader.Channel zoneChannel = ImageLoader.Channel.BLUE;

    @Value("image")
    @Default
    public ImageLoader imageLoader = null;
    private ConfigPack tConfig;

    public WorldConfig(String w, String configID, Terra main) {
        main.registerAllLoaders(LOADER);
        this.worldID = w;
        this.configID = configID;
        load(main);
    }

    public void load(Terra main) {
        tConfig = main.getRegistry().get(configID);

        if(tConfig == null) throw new IllegalStateException("No such config pack \"" + configID + "\"");

        File file = new File(main.getDataFolder() + File.separator + "worlds", worldID + ".yml");

        try {
            if(!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(main.getResource("world.yml")), file);
            }
            LOADER.load(this, new FileInputStream(file));
        } catch(IOException e) {
            throw new IllegalStateException("Unable to load configuration.", e);
        } catch(ConfigException e) {
            throw new IllegalStateException("Unable to proceed due to fatal configuration error.", e);
        }
    }

    public String getWorldID() {
        return worldID;
    }

    public ConfigPack getConfig() {
        return tConfig;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(biomeZChannel.equals(biomeXChannel) || zoneChannel.equals(biomeXChannel) || zoneChannel.equals(biomeZChannel))
            throw new ValidationException("2 objects share the same image channels: biome-x and biome-z");
        return true;
    }
}
