package com.dfsek.terra.mod.attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.dfsek.terra.api.config.ConfigPack;


/**
 * Thread safe.
 */
public final class AttributeReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeReport.class);
    private static final String NOT_A_MINECRAFT_TAG = "no such tag in the minecraft namespace";

    private final String pack;
    private final Consumer<String> warn;
    private final Consumer<String> debug;
    private final Set<String> notApplied = ConcurrentHashMap.newKeySet();

    public AttributeReport(String pack, Consumer<String> warn, Consumer<String> debug) {
        this.pack = Objects.requireNonNull(pack, "pack");
        this.warn = Objects.requireNonNull(warn, "warn");
        this.debug = Objects.requireNonNull(debug, "debug");
    }

    public static AttributeReport logging(ConfigPack pack) {
        return logging(pack, LOGGER);
    }

    public static AttributeReport logging(ConfigPack pack, Logger logger) {
        return new AttributeReport(pack.getID(), logger::warn, logger::debug);
    }

    public void attributeSkipped(String config, String key, String reason) {
        warn.accept("Pack \"" + pack + "\", config \"" + config + "\": attribute \"" + key + "\" skipped: " + reason);
    }

    public void scheduleSkipped(String schedule, String reason) {
        warn.accept("Pack \"" + pack + "\": schedule \"" + schedule + "\" skipped: " + reason);
    }

    public void trackDropped(String schedule, String key, String reason) {
        warn.accept("Pack \"" + pack + "\": schedule \"" + schedule + "\" track \"" + key + "\" dropped: " + reason);
    }

    public void timeMarkerDropped(String schedule, String marker, String clock) {
        warn.accept("Pack \"" + pack + "\": schedule \"" + schedule + "\" time marker \"" + marker + "\" dropped: already defined for "
                    + "clock \"" + clock + "\"");
    }

    public void ignored(String config, String key, String reason) {
        warn.accept("Pack \"" + pack + "\", config \"" + config + "\": \"" + key + "\" ignored: " + reason);
    }

    public void tagCreated(String config, String tag) {
        warn.accept("Pack \"" + pack + "\", config \"" + config + "\": tag \"" + tag + "\" created: " + NOT_A_MINECRAFT_TAG);
    }

    public void notApplied(String key) {
        if(notApplied.add(key)) debug.accept("Pack \"" + pack + "\": attribute \"" + key + "\" is not applied on this platform");
    }
}
