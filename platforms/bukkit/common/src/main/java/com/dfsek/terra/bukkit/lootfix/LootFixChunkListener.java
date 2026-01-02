package com.dfsek.terra.bukkit.lootfix;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

public final class LootFixChunkListener implements Listener {
    private final LootFixService service;

    public LootFixChunkListener(LootFixService service) {
        this.service = service;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        var settings = service.settings();
        if(!settings.enabled || !settings.retroactiveEnabled) return;

        // Run on main thread; ChunkLoadEvent is already synchronous.
        service.fixChunk(event.getChunk(), new Random(event.getWorld().getSeed() ^ event.getChunk().getChunkKey()),
            settings.retroactiveOnlyIfEmpty);
    }
}
