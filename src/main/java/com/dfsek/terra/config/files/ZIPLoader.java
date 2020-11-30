package com.dfsek.terra.config.files;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPLoader extends Loader {
    private final ZipFile file;

    public ZIPLoader(ZipFile file) {
        this.file = file;
    }

    @Override
    protected void load(String directory) {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().startsWith(directory) && entry.getName().endsWith(".yml")) {
                try {
                    streams.add(file.getInputStream(entry));
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
