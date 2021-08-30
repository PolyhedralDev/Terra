package com.dfsek.terra.config.fileloaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZIPLoader extends LoaderImpl {
    private static final Logger logger = LoggerFactory.getLogger(ZIPLoader.class);
    
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
    
    protected void load(String directory, String extension) {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().startsWith(directory) && entry.getName().endsWith(extension)) {
                try {
                    String rel = entry.getName().substring(directory.length());
                    streams.put(rel, file.getInputStream(entry));
                } catch(IOException e) {
                    logger.error("Error while loading file from zip", e);
                }
            }
        }
    }
}
