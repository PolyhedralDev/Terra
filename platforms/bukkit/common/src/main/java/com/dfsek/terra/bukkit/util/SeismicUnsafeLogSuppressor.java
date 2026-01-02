package com.dfsek.terra.bukkit.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Suppresses a known-non-fatal Seismic reflection message on newer JVMs.
 *
 * <p>Seismic attempts to access {@code sun.misc.Unsafe.theUnsafe} for optional optimizations.
 * On some JDKs (notably newer feature releases), reflective access may fail. Seismic logs this as an error,
 * but Terra continues normally via its fallback path.</p>
 *
 * <p>This suppressor hides only the specific log event to keep console output clean on Java 21-25.</p>
 */
public final class SeismicUnsafeLogSuppressor {
    private static final String NEEDLE = "Field theUnsafe not found in class sun.misc.Unsafe";

    private SeismicUnsafeLogSuppressor() { }

    public static void install(JavaPlugin plugin) {
        boolean julOk = installJulFilter();
        boolean log4jOk = installLog4j2Filter();

        if(plugin != null && (julOk || log4jOk)) {
            plugin.getLogger().info("Installed Seismic Unsafe log suppressor (jul=" + julOk + ", log4j2=" + log4jOk + ").");
        }
    }

    private static boolean installJulFilter() {
        try {
            Logger root = Logger.getLogger("");
            for(Handler handler : root.getHandlers()) {
                Filter existing = handler.getFilter();
                handler.setFilter(new SuppressFilter(existing));
            }
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * Best-effort Log4j2 filter install using reflection to avoid a hard dependency on log4j-core.
     *
     * <p>Paper/Purpur typically uses Log4j2 under the hood; this suppresses the same message there as well.</p>
     */
    private static boolean installLog4j2Filter() {
        try {
            Class<?> logManagerClz = Class.forName("org.apache.logging.log4j.LogManager");
            Object ctx = logManagerClz.getMethod("getContext", boolean.class).invoke(null, false);

            // org.apache.logging.log4j.core.LoggerContext
            Class<?> loggerContextClz = Class.forName("org.apache.logging.log4j.core.LoggerContext");
            if(!loggerContextClz.isInstance(ctx)) return false;

            Object configuration = loggerContextClz.getMethod("getConfiguration").invoke(ctx);

            // Configuration#getLoggerConfig(String)
            Class<?> configurationClz = Class.forName("org.apache.logging.log4j.core.config.Configuration");
            Method getLoggerConfig = configurationClz.getMethod("getLoggerConfig", String.class);

            // LoggerConfig#addFilter(Filter)
            Class<?> loggerConfigClz = Class.forName("org.apache.logging.log4j.core.config.LoggerConfig");
            Method addFilter = loggerConfigClz.getMethod("addFilter", Class.forName("org.apache.logging.log4j.core.Filter"));

            Object loggerConfig = getLoggerConfig.invoke(configuration, "com.dfsek.seismic.util.ReflectionUtils");

            // Build a RegexFilter that DENY's the exact message and is NEUTRAL otherwise.
            Class<?> regexFilterClz = Class.forName("org.apache.logging.log4j.core.filter.RegexFilter");
            Class<?> resultClz = Class.forName("org.apache.logging.log4j.core.Filter$Result");

            Object deny = Enum.valueOf((Class<Enum>) resultClz.asSubclass(Enum.class), "DENY");
            Object neutral = Enum.valueOf((Class<Enum>) resultClz.asSubclass(Enum.class), "NEUTRAL");

            // RegexFilter.createFilter(String regex, String[] patternFlags, Boolean useRawMsg, Result match, Result mismatch)
            Method createFilter = regexFilterClz.getMethod(
                "createFilter",
                String.class,
                String[].class,
                Boolean.class,
                resultClz,
                resultClz
            );

            String regex = ".*" + java.util.regex.Pattern.quote(NEEDLE) + ".*";
            Object filter = createFilter.invoke(null, regex, null, Boolean.FALSE, deny, neutral);

            addFilter.invoke(loggerConfig, filter);

            // Apply config updates
            loggerContextClz.getMethod("updateLoggers").invoke(ctx);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static final class SuppressFilter implements Filter {
        private final Filter delegate;

        private SuppressFilter(Filter delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isLoggable(LogRecord record) {
            if(record != null) {
                String msg = record.getMessage();
                if(msg != null && msg.contains(NEEDLE)) {
                    return false;
                }
            }
            return delegate == null || delegate.isLoggable(record);
        }
    }
}
