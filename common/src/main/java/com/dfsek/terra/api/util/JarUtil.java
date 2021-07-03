package com.dfsek.terra.api.util;

import com.dfsek.terra.api.TerraPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {

    private static final AtomicReference<File> terraModJar = new AtomicReference<>(null);

    public static void copyResourcesToDirectory(JarFile fromJar, String sourceDir, String destDir) throws IOException {
        for(Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if(entry.getName().startsWith(sourceDir + "/") && ! entry.isDirectory()) {
                File dest = new File(destDir + File.separator + entry.getName().substring(sourceDir.length() + 1));
                if(dest.exists()) continue;
                File parent = dest.getParentFile();
                if(parent != null) {
                    parent.mkdirs();
                }
                try(FileOutputStream out = new FileOutputStream(dest); InputStream in = fromJar.getInputStream(entry)) {
                    byte[] buffer = new byte[(8192)];

                    int s;
                    while((s = in.read(buffer)) > 0) {
                        out.write(buffer, 0, s);
                    }
                } catch(IOException e) {
                    throw new IOException("Could not copy asset from jar file", e);
                }
            }
        }
    }

    public static JarFile getJarFile() throws URISyntaxException, IOException {
        final File _jarFile1 = terraModJar.get();
        if(_jarFile1 != null) return new JarFile(_jarFile1);
        synchronized(terraModJar) {
            final File _jarFile2 = terraModJar.get();
            if(_jarFile2 != null) return new JarFile(_jarFile2);

            // uncached here
            final URI jarURI = getJarURL().toURI();
            if(jarURI.getScheme() == null || !jarURI.getScheme().equalsIgnoreCase("file")) {
                final File tempFile = File.createTempFile("terra-jar-" + UUID.randomUUID(), ".jar");
                try(final FileOutputStream out = new FileOutputStream(tempFile)) {
                    Files.copy(Paths.get(jarURI), out);
                }
                tempFile.deleteOnExit();
                terraModJar.set(tempFile);
                return new JarFile(tempFile);
            } else {
                final File jarFile = new File(jarURI);
                terraModJar.set(jarFile);
                return new JarFile(jarFile);
            }
        }
    }

    public static URL getJarURL() {
        return TerraPlugin.class.getProtectionDomain().getCodeSource().getLocation();
    }
}
