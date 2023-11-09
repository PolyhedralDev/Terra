package com.dfsek.terra.quilt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;


/**
 * So you want to Mixin into Authlib/Brigadier/DataFixerUpper, on Fabric you'll need this guy.
 *
 * <p>YOU SHOULD ONLY USE THIS CLASS DURING "preLaunch" and ONLY TARGET A CLASS WHICH IS NOT ANY CLASS YOU MIXIN TO.
 * <p>
 * This will likely not work on Gson because FabricLoader has some special logic related to Gson.
 */
public final class AwfulQuiltHacks {
    private static final ClassLoader KNOT_CLASSLOADER = Thread.currentThread().getContextClassLoader();
    private static final Method ADD_URL_METHOD;
    static {
        Method tempAddUrlMethod = null;
        try {
            tempAddUrlMethod = KNOT_CLASSLOADER.getClass().getMethod("addURL", URL.class);
            tempAddUrlMethod.setAccessible(true);
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException("Failed to load Classloader fields", e);
        }

        ADD_URL_METHOD = tempAddUrlMethod;
    }

    private AwfulQuiltHacks() { }

    /**
     * Hackily load the package which a mixin may exist within.
     * <p>
     * YOU SHOULD NOT TARGET A CLASS WHICH YOU MIXIN TO.
     *
     * @param pathOfAClass The path of any class within the package.
     */
    public static void hackilyLoadForMixin(String pathOfAClass)
    throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        URL url = Class.forName(pathOfAClass).getProtectionDomain().getCodeSource().getLocation();
        ADD_URL_METHOD.invoke(KNOT_CLASSLOADER, url);
    }
}
