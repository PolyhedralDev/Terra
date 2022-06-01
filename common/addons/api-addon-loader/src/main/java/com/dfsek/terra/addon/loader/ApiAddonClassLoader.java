/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addon.loader;

import java.net.URL;
import java.net.URLClassLoader;


public class ApiAddonClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }
    
    public ApiAddonClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
