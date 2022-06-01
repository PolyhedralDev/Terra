/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events;

/**
 * An event which (optionally) passes exceptions thrown by listeners to
 * the event caller.
 */
public interface FailThroughEvent extends Event {
}
