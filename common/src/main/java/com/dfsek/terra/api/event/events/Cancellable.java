package com.dfsek.terra.api.event.events;

/**
 * Events that implement this interface may be cancelled.
 * <p>
 * Cancelling an event is assumed to stop the execution of whatever action triggered the event.
 */
public interface Cancellable extends Event {
    /**
     * Get the cancellation status of the event.
     *
     * @return Whether event is cancelled.
     */
    boolean isCancelled();

    /**
     * Set the cancellation status of the event.
     *
     * @param cancelled Whether event is cancelled.
     */
    void setCancelled(boolean cancelled);
}
