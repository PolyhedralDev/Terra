/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import java.io.File;


public class StringUtil {
    public static String fileName(String path) {
        if(path.contains(File.separator)) {
            return path.substring(path.lastIndexOf(File.separatorChar) + 1, path.lastIndexOf('.'));
        } else if(path.contains("/")) {
            return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf('.'));
        } else if(path.contains(".")) {
            return path.substring(0, path.lastIndexOf('.'));
        } else {
            return path;
        }
    }
}
