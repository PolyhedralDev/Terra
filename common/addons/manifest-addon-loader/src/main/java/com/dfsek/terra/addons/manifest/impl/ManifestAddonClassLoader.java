/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl;

import java.net.URL;
import java.net.URLClassLoader;


public class ManifestAddonClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }
    
    public ManifestAddonClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
