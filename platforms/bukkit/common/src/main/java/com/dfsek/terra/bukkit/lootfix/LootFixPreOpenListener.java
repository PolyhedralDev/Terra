package com.dfsek.terra.bukkit.lootfix;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public final class LootFixPreOpenListener implements Listener {
    private final LootFixService service;

    public LootFixPreOpenListener(LootFixService service) {
        this.service = service;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = event.getClickedBlock();
        if(b == null) return;

        Material t = b.getType();
        if(t != Material.CHEST && t != Material.TRAPPED_CHEST) return;

        // A lightweight repair pass for the chunk; this ensures loot tables exist before other plugins (e.g., Lootin).
        service.fixChunk(b.getChunk(), new Random(b.getWorld().getSeed() ^ b.getChunk().getChunkKey()), true);
    }
}
