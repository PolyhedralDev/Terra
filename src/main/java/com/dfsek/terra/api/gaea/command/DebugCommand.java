package com.dfsek.terra.api.gaea.command;

/**
 * Implementing this interface marks a command as debug-only.
 * If a parent command implements this interface, all child commands will be considered debug commands, regardless of whether they implement DebugCommand as well.
 */
public interface DebugCommand {
}
