package com.dfsek.terra.api.event.events;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.annotations.Global;

/**
 * An event with functionality directly linked to a {@link ConfigPack}.
 * <p>
 * PackEvents are only invoked when the pack specifies the com.dfsek.terra.addon in its
 * {@code com.dfsek.terra.addon} key (or when the listener is annotated {@link Global}).
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface PackEvent extends Event {
    /**
     * Get the {@link ConfigPack} associated with this event.
     *
     * @return ConfigPack associated with the event.
     */
    ConfigPack getPack();
}
