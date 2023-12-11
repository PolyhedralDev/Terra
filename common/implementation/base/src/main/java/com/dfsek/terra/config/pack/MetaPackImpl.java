package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.yaml.YamlConfiguration;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.config.MetaPack;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class MetaPackImpl implements MetaPack {
    private final Platform platform;

    private final MetaPackTemplate template = new MetaPackTemplate();

    @Override
    public String getAuthor() {
        return template.getAuthor();
    }

    @Override
    public Version getVersion() {
        return template.getVersion();
    }

    @Override
    public Map<String, ConfigPack> packs() {
        return null;
    }
}
