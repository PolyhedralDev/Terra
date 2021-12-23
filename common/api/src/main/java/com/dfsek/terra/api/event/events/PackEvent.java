/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events;

import com.dfsek.terra.api.config.ConfigPack;


/**
 * An event with functionality directly linked to a {@link ConfigPack}.
 * <p>
 * PackEvents are only invoked when the pack specifies the addon in its
 * {@code addon} key (or when the listener is global).
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
