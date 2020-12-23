package com.dfsek.terra.config.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPLoader extends Loader {
    private final ZipFile file;

    public ZIPLoader(ZipFile file) {
        this.file = file;
    }

    @Override
    public InputStream get(String singleFile) throws IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().equals(singleFile)) return file.getInputStream(entry);
        }
        throw new IllegalArgumentException("No such file: " + singleFile);
    }

    @Override
    protected void load(String directory) {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().startsWith(directory) && entry.getName().endsWith(".yml")) {
                try {
                    streams.put(entry.getName(), file.getInputStream(entry));
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
