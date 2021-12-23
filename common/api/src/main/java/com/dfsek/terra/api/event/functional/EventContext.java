/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.functional;

import java.util.function.Consumer;

import com.dfsek.terra.api.event.events.Event;


public interface EventContext<T extends Event> {
    EventContext<T> then(Consumer<T> action);
    
    EventContext<T> priority(int priority);
    
    EventContext<T> failThrough();
    
    EventContext<T> global();
}
