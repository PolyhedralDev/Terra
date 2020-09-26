package com.dfsek.terra.structure;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class StructureManager {
    private static StructureManager singleton;
    private Logger logger;
    private final Map<UUID, GaeaStructure> structures = new HashMap<>();
    public StructureManager(JavaPlugin main) {
        if(singleton!= null) throw new IllegalStateException("Only one instance of StructureManager may exist at a given time.");
        this.logger = main.getLogger();
        logger.info("Initializing StructureManager...");
        singleton = this;
    }
    public GaeaStructure get(UUID uuid) {
        return structures.get(uuid);
    }
    public void load(File file) throws IOException {
        GaeaStructure s = GaeaStructure.load(file);
        structures.put(s.getUuid(), s);
    }
}
