package com.dfsek.terra.api.event.events;

import com.dfsek.terra.api.util.mutable.MutableBoolean;


/**
 * Abstract class containing basic {@link Cancellable} implementation.
 */
public abstract class AbstractCancellable implements Cancellable {
    private final MutableBoolean cancelled = new MutableBoolean(false);
    
    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled.set(cancelled);
    }
}
