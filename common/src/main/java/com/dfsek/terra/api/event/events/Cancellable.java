package com.dfsek.terra.api.event.events;

/**
 * Events that implement this interface may be cancelled.
 *
 * Cancelling an event is assumed to stop the execution of whatever action triggered the event.
 */
public interface Cancellable extends Event {
    boolean isCancelled();
    void setCancelled();
}
