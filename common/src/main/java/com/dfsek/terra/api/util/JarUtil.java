package com.dfsek.terra.api.util;

import com.dfsek.terra.api.TerraPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {
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
        return new JarFile(new File(getJarURL().toURI()));
    }

    public static URL getJarURL() {
        return TerraPlugin.class.getProtectionDomain().getCodeSource().getLocation();
    }
}
