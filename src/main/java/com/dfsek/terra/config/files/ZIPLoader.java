package com.dfsek.terra.config.files;

import com.dfsek.tectonic.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPLoader implements Loader {
    private final List<InputStream> streams = new ArrayList<>();

    public ZIPLoader(ZipFile file, String subDirectory) {
        Enumeration<? extends ZipEntry> entries = file.entries();

        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().startsWith(subDirectory) && entry.getName().endsWith(".yml")) {
                try {
                    streams.add(file.getInputStream(entry));
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ZIPLoader then(ExceptionalConsumer<List<InputStream>> consumer) throws ConfigException {
        consumer.accept(streams);
        return this;
    }

    @Override
    public void close() {
        streams.forEach(stream -> {
            try {
                stream.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        streams.clear();
    }
}
