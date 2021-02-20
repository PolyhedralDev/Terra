package com.dfsek.terra.api.core.event.events;

import com.dfsek.terra.config.pack.ConfigPack;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface PackEvent extends Event {
    ConfigPack getPack();
}
