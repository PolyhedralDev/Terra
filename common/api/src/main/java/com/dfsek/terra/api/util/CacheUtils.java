package com.dfsek.terra.api.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CacheUtils {
    public static final Executor CACHE_EXECUTOR = Executors.newSingleThreadExecutor();
}
